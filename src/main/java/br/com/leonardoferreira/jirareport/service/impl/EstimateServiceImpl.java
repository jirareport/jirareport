package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.EstimateForm;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;
import br.com.leonardoferreira.jirareport.domain.vo.Percentile;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public List<EstimateIssue> findEstimateIssues(Long boardId, EstimateForm estimateForm) {
        if (estimateForm.getStartDate() == null || estimateForm.getEndDate() == null) {
            return Collections.emptyList();
        }


        final Board board = boardRepository.findById(boardId).orElseThrow(ResourceNotFound::new);

        List<EstimateIssue> issueList = issueService.findByJql(searchJQL(board), board);

        final List<String> holidays = board.getIgnoreWeekend() ?
                Collections.emptyList() :
                holidayService.findByBoard(boardId)
                        .stream().map(Holiday::getEnDate).collect(Collectors.toList());

        // Calçular o que precisar a estimativa
        Map<String, Percentile> fieldPercentileMap = new HashMap<>();
        issueList.forEach(
                issue -> {
                    Percentile percentile = fieldPercentileMap.get(issue.getIssueType());
                    if (percentile == null) {
                        percentile = calculatePercentile(board, estimateForm, issue.getIssueType());
                        fieldPercentileMap.put(issue.getIssueType(), percentile);
                    }
                    // TODO Rever como calcular o long do average
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

    private Percentile calculatePercentile(Board board, EstimateForm estimateForm, String field) {

        IssueForm issueForm = new IssueForm();
        issueForm.setStartDate(estimateForm.getStartDate());
        issueForm.setEndDate(estimateForm.getEndDate());
        issueForm.setIssueTypes(Arrays.asList(field));

        List<Issue> issues = issueRepository.findByExample(board.getId(), issueForm);
        Double avgLeadTime = issues.parallelStream()
                .filter(i -> i.getLeadTime() != null)
                .mapToLong(Issue::getLeadTime)
                .average().orElse(0D);
        issues.sort(Comparator.comparing(Issue::getLeadTime));

        Long median = 0L;
        Long percentile75 = 0L;
        Long percentile90 = 0L;

        if (!issues.isEmpty()) {
            int medianIndex = CalcUtil.calculateCeilingPercentage(issues.size(), 50);
            int percentile75Index = CalcUtil.calculateCeilingPercentage(issues.size(), 75);
            int percentile90Index = CalcUtil.calculateCeilingPercentage(issues.size(), 90);

            median = issues.get(medianIndex - 1).getLeadTime();
            percentile75 = issues.get(percentile75Index - 1).getLeadTime();
            percentile90 = issues.get(percentile90Index - 1).getLeadTime();
        }

        return Percentile.builder()
                .average(avgLeadTime)
                .median(median)
                .percentile75(percentile75)
                .percentile90(percentile90)
                .build();
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
