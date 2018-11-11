package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.EstimateFieldReference;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.EstimateForm;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;
import br.com.leonardoferreira.jirareport.domain.vo.Percentile;
import br.com.leonardoferreira.jirareport.exception.InternalServerErrorException;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.BoardRepository;
import br.com.leonardoferreira.jirareport.repository.IssueRepository;
import br.com.leonardoferreira.jirareport.service.EstimateService;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.util.CalcUtil;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import br.com.leonardoferreira.jirareport.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EstimateServiceImpl implements EstimateService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private HolidayService holidayService;

    @Override
    public List<EstimateIssue> findEstimateIssues(final Long boardId, final EstimateForm estimateForm) {
        if (estimateForm.getStartDate() == null || estimateForm.getEndDate() == null) {
            return Collections.emptyList();
        }

        final Board board = boardRepository.findById(boardId).orElseThrow(ResourceNotFound::new);

        List<EstimateIssue> issueList = issueService.findByJql(searchJQL(board), board);

        final List<String> holidays = board.getIgnoreWeekend()
                ? Collections.emptyList()
                : holidayService.findByBoard(boardId)
                        .stream().map(Holiday::getEnDate).collect(Collectors.toList());

        // Calçular o que precisar a estimativa
        final Map<String, Percentile> fieldPercentileMap = new HashMap<>();
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
                            percentile.getPercentile75().longValue(), holidays, board.getIgnoreWeekend()));
                    issue.setEstimateDatePercentile90(DateUtil.addDays(issue.getStartDate(),
                            percentile.getPercentile90().longValue(), holidays, board.getIgnoreWeekend()));
                }
        );

        return issueList;
    }

    private Percentile calculatePercentile(final Board board, final EstimateForm estimateForm, final String value) {

        IssueForm issueForm = buildIssueFormByEstimateForm(estimateForm, value);

        List<Issue> issues = issueRepository.findByExample(board.getId(), issueForm);

        List<Long> leadTimeList = issues.stream().map(Issue::getLeadTime).collect(Collectors.toList());

        return CalcUtil.calculatePercentile(leadTimeList);
    }

    private IssueForm buildIssueFormByEstimateForm(final EstimateForm estimateForm, final String value) {
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

        // Precisa tirar a última coluna
        Set<String> fluxColumns = CalcUtil.calcWipColumns(board);
        if (fluxColumns == null || fluxColumns.isEmpty()) {
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
        params.put("fluxColumns", fluxColumns);

        return StringUtil.replaceParams(jql.toString(), params);
    }
}
