package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.FluxColumn;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.EstimateFieldReference;
import br.com.jiratorio.domain.form.EstimateForm;
import br.com.jiratorio.domain.form.IssueForm;
import br.com.jiratorio.domain.EstimateIssue;
import br.com.jiratorio.domain.Percentile;
import br.com.jiratorio.exception.InternalServerErrorException;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.EstimateService;
import br.com.jiratorio.service.HolidayService;
import br.com.jiratorio.service.IssueService;
import br.com.jiratorio.service.PercentileService;
import br.com.jiratorio.util.DateUtil;
import br.com.jiratorio.util.StringUtil;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class EstimateServiceImpl implements EstimateService {

    private final BoardService boardService;

    private final IssueService issueService;

    private final HolidayService holidayService;

    private final PercentileService percentileService;

    public EstimateServiceImpl(final BoardService boardService,
                               final IssueService issueService,
                               final HolidayService holidayService,
                               final PercentileService percentileService) {
        this.boardService = boardService;
        this.issueService = issueService;
        this.holidayService = holidayService;
        this.percentileService = percentileService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstimateIssue> findEstimateIssues(final Long boardId, final EstimateForm estimateForm) {
        log.info("Method=findEstimateIssues, boardId={}, estimateForm={}", boardId, estimateForm);

        if (estimateForm.getStartDate() == null || estimateForm.getEndDate() == null) {
            return Collections.emptyList();
        }

        Board board = boardService.findById(boardId);

        List<EstimateIssue> issueList = issueService.findEstimateByJql(searchJQL(board), board);

        List<LocalDate> holidays = board.getIgnoreWeekend()
                ? Collections.emptyList()
                : holidayService.findDaysByBoard(boardId);

        Map<String, Percentile> fieldPercentileMap = new HashMap<>();
        issueList.forEach(
                issue -> {
                    String value = retrieveByFilter(issue, estimateForm.getFilter());
                    Percentile percentile = fieldPercentileMap.get(value);
                    if (percentile == null) {
                        percentile = calculatePercentile(board, estimateForm, value);
                        fieldPercentileMap.put(value, percentile);
                    }
                    issue.setEstimateDateAvg(DateUtil.addDays(issue.getStartDate(),
                            percentile.getAverage().longValue(), holidays, board.getIgnoreWeekend()));
                    issue.setEstimateDatePercentile50(DateUtil.addDays(issue.getStartDate(),
                            percentile.getMedian(), holidays, board.getIgnoreWeekend()));
                    issue.setEstimateDatePercentile75(DateUtil.addDays(issue.getStartDate(),
                            percentile.getPercentile75(), holidays, board.getIgnoreWeekend()));
                    issue.setEstimateDatePercentile90(DateUtil.addDays(issue.getStartDate(),
                            percentile.getPercentile90(), holidays, board.getIgnoreWeekend()));
                }
        );

        return issueList;
    }

    private Percentile calculatePercentile(final Board board, final EstimateForm estimateForm, final String value) {
        log.info("Method=calculatePercentile, board={}, estimateForm={}, value={}", board, estimateForm, value);

        IssueForm issueForm = buildIssueFormByEstimateForm(estimateForm, value);

        List<Long> leadTimeList = issueService.findLeadTimeByExample(board.getId(), issueForm);

        return percentileService.calculatePercentile(leadTimeList);
    }

    private IssueForm buildIssueFormByEstimateForm(final EstimateForm estimateForm, final String value) {
        log.info("Method=buildIssueFormByEstimateForm, estimateForm={}, value={}", estimateForm, value);

        IssueForm issueForm = new IssueForm();
        issueForm.setStartDate(estimateForm.getStartDate());
        issueForm.setEndDate(estimateForm.getEndDate());
        EstimateFieldReference filter = estimateForm.getFilter();
        if (filter == null || StringUtils.isEmpty(value)) {
            return issueForm;
        }

        if (filter.equals(EstimateFieldReference.ISSUE_TYPE)) {
            issueForm.getIssueTypes().add(value);
        } else if (filter.equals(EstimateFieldReference.SYSTEM)) {
            issueForm.getSystems().add(value);
        } else if (filter.equals(EstimateFieldReference.TASK_SIZE)) {
            issueForm.getTaskSize().add(value);
        } else if (filter.equals(EstimateFieldReference.EPIC)) {
            issueForm.getEpics().add(value);
        } else if (filter.equals(EstimateFieldReference.PROJECT)) {
            issueForm.getProjects().add(value);
        } else if (filter.equals(EstimateFieldReference.PRIORITY)) {
            issueForm.getPriorities().add(value);
        }
        return issueForm;
    }

    private String retrieveByFilter(final EstimateIssue issue, final EstimateFieldReference filter) {
        log.info("Method=retrieveByFilter, issue={}, filter={}", issue, filter);

        if (filter == null) {
            return null;
        }
        if (filter.equals(EstimateFieldReference.ISSUE_TYPE)) {
            return issue.getIssueType();
        }
        if (filter.equals(EstimateFieldReference.SYSTEM)) {
            return issue.getSystem();
        }
        if (filter.equals(EstimateFieldReference.TASK_SIZE)) {
            return issue.getEstimated();
        }
        if (filter.equals(EstimateFieldReference.EPIC)) {
            return issue.getEpic();
        }
        if (filter.equals(EstimateFieldReference.PROJECT)) {
            return issue.getProject();
        }
        if (filter.equals(EstimateFieldReference.PRIORITY)) {
            return issue.getPriority();
        }
        return null;
    }

    private String searchJQL(final Board board) {
        log.info("Method=searchJQL, board={}", board);

        FluxColumn fluxColumn = new FluxColumn(board);

        Set<String> wipColumns = fluxColumn.getWipColumns();
        if (CollectionUtils.isEmpty(wipColumns)) {
            throw new InternalServerErrorException("O fluxo de colunas não está configurado");
        }

        Map<String, Object> params = new HashMap<>();

        StringBuilder jql = new StringBuilder();
        jql.append(" project = {project} ");

        if (board.getIgnoreIssueType() != null && !board.getIgnoreIssueType().isEmpty()) {
            jql.append(" AND issueType NOT IN ({issueTypes}) ");
            params.put("issueTypes", board.getIgnoreIssueType());
        }

        jql.append(" AND status IN ({fluxColumns}) ");

        params.put("project", board.getExternalId().toString());
        params.put("fluxColumns", wipColumns);

        return StringUtil.replaceParams(jql.toString(), params);
    }
}
