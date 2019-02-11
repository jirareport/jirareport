package br.com.jiratorio.service.impl;

import br.com.jiratorio.repository.IssueRepository;
import br.com.jiratorio.aspect.annotation.ExecutionTime;
import br.com.jiratorio.client.IssueClient;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.Issue;
import br.com.jiratorio.domain.embedded.Chart;
import br.com.jiratorio.domain.form.IssueForm;
import br.com.jiratorio.domain.form.IssuePeriodForm;
import br.com.jiratorio.domain.vo.ChartAggregator;
import br.com.jiratorio.domain.vo.EstimateIssue;
import br.com.jiratorio.domain.vo.SandBox;
import br.com.jiratorio.domain.vo.SandBoxFilter;
import br.com.jiratorio.mapper.EstimateIssueMapper;
import br.com.jiratorio.mapper.IssueMapper;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.ChartService;
import br.com.jiratorio.service.IssueService;
import br.com.jiratorio.service.LeadTimeService;
import br.com.jiratorio.util.CalcUtil;
import br.com.jiratorio.util.DateUtil;
import br.com.jiratorio.util.StringUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class IssueServiceImpl extends AbstractService implements IssueService {

    @Autowired
    private IssueClient issueClient;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private EstimateIssueMapper estimateIssueMapper;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ChartService chartService;

    @Autowired
    private LeadTimeService leadTimeService;

    @Autowired
    private BoardService boardService;

    @Override
    @ExecutionTime
    @Transactional
    public List<Issue> createByJql(final String jql, final Board board) {
        log.info("Method=createByJql, jql={}, board={}", jql, board);

        String issuesStr = issueClient.findAll(currentToken(), jql);

        List<Issue> issues = issueMapper.parse(issuesStr, board);

        issueRepository.saveAll(issues);
        leadTimeService.createLeadTimes(issues, board.getId());

        return issues;
    }

    @Override
    public List<EstimateIssue> findEstimateByJql(final String jql, final Board board) {
        log.info("Method=findEstimateByJql, jql={}, board={}", jql, board);
        String issuesStr = issueClient.findAll(currentToken(), jql);

        return estimateIssueMapper.parseEstimate(issuesStr, board);
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public SandBox findByExample(final Long boardId, final IssueForm issueForm) {
        log.info("Method=findByExample, boardId={}, issueForm={}", boardId, issueForm);

        if (issueForm.getStartDate() == null || issueForm.getEndDate() == null) {
            return new SandBox();
        }

        List<Issue> issues = issueRepository.findByExample(boardId, issueForm);

        Board board = boardService.findById(boardId);
        ChartAggregator chartAggregator = chartService.buildAllCharts(issues, board);

        Double avgLeadTime = issues.parallelStream()
                .filter(i -> i.getLeadTime() != null)
                .mapToLong(Issue::getLeadTime)
                .average().orElse(0D);

        Chart<String, Long> weeklyThroughput = calcWeeklyThroughput(issueForm, issues);

        return SandBox.builder()
                .issues(issues)
                .chartAggregator(chartAggregator)
                .avgLeadTime(avgLeadTime)
                .weeklyThroughput(weeklyThroughput)
                .build();
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public List<Long> findLeadTimeByExample(final Long boardId, final IssueForm issueForm) {
        log.info("Method=findLeadTimeByExample, board={}, issueForm={}", boardId, issueForm);

        return issueRepository.findByExample(boardId, issueForm)
                .stream()
                .map(Issue::getLeadTime)
                .collect(Collectors.toList());
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public SandBoxFilter findSandBoxFilters(final Long boardId, final SandBox sandBox, final IssueForm issueForm) {
        log.info("Method=findSandBoxFilters, boardId={}, sandBox={}, issueForm={}", boardId, sandBox, issueForm);

        return SandBoxFilter.builder()
                .estimatives(issueRepository.findAllEstimativesByBoardId(boardId))
                .keys(findAllKeys(sandBox, issueForm))
                .systems(issueRepository.findAllSystemsByBoardId(boardId))
                .epics(issueRepository.findAllEpicsByBoardId(boardId))
                .issueTypes(issueRepository.findAllIssueTypesByBoardId(boardId))
                .projects(issueRepository.findAllIssueProjectsByBoardId(boardId))
                .priorities(issueRepository.findAllIssuePrioritiesByBoardId(boardId))
                .dynamicFieldsValues(issueRepository.findAllDynamicFieldValues(boardId))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> findByIssuePeriodId(final Long issuePeriodId) {
        log.info("Method=findByIssuePeriodId, issuePeriodId={}", issuePeriodId);

        return issueRepository.findByIssuePeriodId(issuePeriodId);
    }

    @Override
    @Transactional(readOnly = true)
    public String searchJQL(final IssuePeriodForm issuePeriodForm, final Board board) {
        log.info("Method=searchJQL, issuePeriodForm={}, board={}", issuePeriodForm, board);

        final List<String> fluxColumn = board.getFluxColumn();
        String lastColumn = fluxColumn == null || fluxColumn.isEmpty() ? "Done" : fluxColumn.get(fluxColumn.size() - 1);

        Map<String, Object> params = new HashMap<>();

        StringBuilder jql = new StringBuilder();
        jql.append(" project = {project} ");
        jql.append(" AND ( STATUS CHANGED TO {endColumn} DURING({startDate}, {endDate}) ");
        jql.append("       OR ( STATUS CHANGED TO {lastColumn} DURING ({startDate}, {endDate}) AND NOT STATUS CHANGED TO {endColumn} )");
        jql.append("       OR ( resolutiondate >= {startDate} AND resolutiondate <= {endDate} AND NOT STATUS CHANGED TO {endColumn} ");
        jql.append("              AND NOT STATUS CHANGED TO {endColumn} )");
        jql.append("     ) ");

        if (board.getIgnoreIssueType() != null && !board.getIgnoreIssueType().isEmpty()) {
            jql.append(" AND issueType NOT IN ({issueTypes}) ");
            params.put("issueTypes", board.getIgnoreIssueType());
        }

        jql.append(" AND status WAS IN ({startColumns}) ");
        jql.append(" AND status IN ({endColumns}) ");

        params.put("project", board.getExternalId().toString());
        params.put("startDate", DateUtil.toENDate(issuePeriodForm.getStartDate()));
        params.put("endDate", DateUtil.toENDate(issuePeriodForm.getEndDate()) + " 23:59");
        params.put("lastColumn", lastColumn);
        params.put("endColumn", board.getEndColumn());
        params.put("endColumns", CalcUtil.calcEndColumns(board));
        params.put("startColumns", CalcUtil.calcStartColumns(board));

        return StringUtil.replaceParams(jql.toString(), params);
    }

    @Override
    @Transactional
    public void deleteAll(final List<Issue> issues) {
        log.info("Method=deleteAll, issues={}", issues);
        issueRepository.deleteAll(issues);
    }

    private List<String> findAllKeys(final SandBox sandBox, final IssueForm issueForm) {
        return Stream.concat(sandBox.getIssues().stream().map(Issue::getKey), issueForm.getKeys().stream())
                .distinct().sorted().collect(Collectors.toList());
    }

    private Chart<String, Long> calcWeeklyThroughput(final IssueForm issueForm, final List<Issue> issues) {
        Chart<String, Long> chart = new Chart<>();

        TemporalField temporalField = WeekFields.of(new Locale("pt-BR")).dayOfWeek();
        LocalDate currentDate = issueForm.getStartDate().with(temporalField, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (currentDate.isBefore(issueForm.getEndDate())) {
            LocalDate startWeek = currentDate;
            currentDate = currentDate.plusWeeks(1);
            LocalDate endWeek = currentDate;

            long issuesCount = issues.stream()
                    .filter(i -> i.getEndDate().isAfter(startWeek.atStartOfDay()) && i.getEndDate().isBefore(endWeek.atStartOfDay()))
                    .count();

            String week = String.format("[%s - %s]", startWeek.format(formatter), endWeek.format(formatter));
            chart.add(week, issuesCount);
        }

        return chart;
    }
}
