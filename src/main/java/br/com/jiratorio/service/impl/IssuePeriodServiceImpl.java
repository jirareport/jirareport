package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.FluxColumn;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.Issue;
import br.com.jiratorio.domain.entity.IssuePeriod;
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest;
import br.com.jiratorio.domain.chart.ChartAggregator;
import br.com.jiratorio.domain.IssueCountBySize;
import br.com.jiratorio.domain.IssuePeriodChart;
import br.com.jiratorio.domain.IssuePeriodDetails;
import br.com.jiratorio.domain.response.IssuePeriodResponse;
import br.com.jiratorio.domain.chart.LeadTimeCompareChart;
import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.mapper.IssuePeriodMapper;
import br.com.jiratorio.repository.IssuePeriodRepository;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.ChartService;
import br.com.jiratorio.service.IssuePeriodService;
import br.com.jiratorio.service.IssueService;
import br.com.jiratorio.service.JQLService;
import br.com.jiratorio.service.WipService;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class IssuePeriodServiceImpl extends AbstractService implements IssuePeriodService {

    private final IssueService issueService;

    private final IssuePeriodRepository issuePeriodRepository;

    private final ChartService chartService;

    private final BoardService boardService;

    private final IssuePeriodMapper issuePeriodMapper;

    private final WipService wipService;

    private final JQLService jqlService;

    public IssuePeriodServiceImpl(final IssueService issueService,
                                  final IssuePeriodRepository issuePeriodRepository,
                                  final ChartService chartService,
                                  final BoardService boardService,
                                  final IssuePeriodMapper issuePeriodMapper,
                                  final WipService wipService,
                                  final JQLService jqlService) {
        this.issueService = issueService;
        this.issuePeriodRepository = issuePeriodRepository;
        this.chartService = chartService;
        this.boardService = boardService;
        this.issuePeriodMapper = issuePeriodMapper;
        this.wipService = wipService;
        this.jqlService = jqlService;
    }

    @Override
    @Transactional
    public Long create(final CreateIssuePeriodRequest createIssuePeriodRequest, final Long boardId) {
        log.info("Method=create, createIssuePeriodRequest={}, boardId={}", createIssuePeriodRequest, boardId);

        delete(createIssuePeriodRequest, boardId);

        Board board = boardService.findById(boardId);

        String jql = jqlService.finalizedIssues(board, createIssuePeriodRequest.getStartDate(), createIssuePeriodRequest.getEndDate());
        List<Issue> issues = issueService.createByJql(jql, board);

        Double avgLeadTime = issues.parallelStream()
                .filter(i -> Objects.nonNull(i.getLeadTime()))
                .mapToLong(Issue::getLeadTime)
                .average().orElse(0D);

        Double avgPctEfficiency = issues.parallelStream()
                .filter(i -> Objects.nonNull(i.getPctEfficiency()))
                .mapToDouble(Issue::getPctEfficiency)
                .average().orElse(0D);

        ChartAggregator chartAggregator = chartService.buildAllCharts(issues, board);

        FluxColumn fluxColumn = new FluxColumn(board);
        Double wipAvg = wipService.calcAvgWip(createIssuePeriodRequest.getStartDate(), createIssuePeriodRequest.getEndDate(),
                issues, fluxColumn.getStartColumns());

        IssuePeriodDetails details = new IssuePeriodDetails(avgLeadTime, issues.size(), boardId, jql, wipAvg, avgPctEfficiency);

        IssuePeriod issuePeriod = issuePeriodMapper.fromJiraData(createIssuePeriodRequest, issues,
                chartAggregator, details);

        issuePeriodRepository.save(issuePeriod);

        return issuePeriod.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssuePeriod> findByBoardId(final Long boardId) {
        log.info("Method=findByBoardId, boardId={}", boardId);

        List<IssuePeriod> issuePeriods = issuePeriodRepository.findByBoardId(boardId);
        issuePeriods.sort(Comparator.comparing(IssuePeriod::getStartDate));

        return issuePeriods;
    }

    @Override
    @Transactional(readOnly = true)
    public IssuePeriodChart buildCharts(final List<IssuePeriod> issuePeriods, final Board board) {
        log.info("Method=buildCharts, issuePeriods={}", issuePeriods);

        IssuePeriodChart issuePeriodChart = new IssuePeriodChart();
        for (IssuePeriod issuePeriod : issuePeriods) {
            issuePeriodChart.addLeadTime(issuePeriod);
            issuePeriodChart.addIssuesCount(issuePeriod);
        }

        IssueCountBySize issueCountBySize = chartService.buildIssueCountBySize(issuePeriods);
        issuePeriodChart.setIssueCountBySize(issueCountBySize);

        LeadTimeCompareChart leadTimeCompareChart = chartService.calcLeadTimeCompareByPeriod(issuePeriods, board);
        issuePeriodChart.setLeadTimeCompareChart(leadTimeCompareChart);

        return issuePeriodChart;
    }

    @Override
    @Transactional(readOnly = true)
    public IssuePeriod findById(final Long id) {
        log.info("Method=findById, id={}", id);

        return issuePeriodRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    @Transactional
    public void remove(final Long id) {
        log.info("Method=remove, id={}", id);

        IssuePeriod issuePeriod = findById(id);
        delete(issuePeriod);
    }

    @Override
    @Transactional
    public void update(final Long issuePeriodId) {
        log.info("Method=update, issuePeriodId={}", issuePeriodId);

        IssuePeriod issuePeriod = findById(issuePeriodId);
        CreateIssuePeriodRequest createIssuePeriodRequest = new CreateIssuePeriodRequest(issuePeriod.getStartDate(), issuePeriod.getEndDate());

        create(createIssuePeriodRequest, issuePeriod.getBoardId());
    }

    @Override
    @Transactional(readOnly = true)
    public IssuePeriodResponse findIssuePeriodsAndCharts(final Long boardId) {
        Board board = boardService.findById(boardId);
        List<IssuePeriod> issuePeriods = findByBoardId(boardId);
        IssuePeriodChart issuePeriodChart = buildCharts(issuePeriods, board);

        return IssuePeriodResponse.builder()
                .issuePeriods(issuePeriods)
                .issuePeriodChart(issuePeriodChart)
                .build();
    }

    private void delete(final CreateIssuePeriodRequest createIssuePeriodRequest, final Long boardId) {
        log.info("Method=delete, createIssuePeriodRequest={}, boardId={}", createIssuePeriodRequest, boardId);

        IssuePeriod issuePeriod = issuePeriodRepository.findByStartDateAndEndDateAndBoardId(createIssuePeriodRequest.getStartDate(),
                createIssuePeriodRequest.getEndDate(), boardId);

        if (issuePeriod != null) {
            delete(issuePeriod);
        }
    }

    private void delete(final IssuePeriod issuePeriod) {
        issuePeriodRepository.delete(issuePeriod);
        issueService.deleteAll(issuePeriod.getIssues());

    }
}
