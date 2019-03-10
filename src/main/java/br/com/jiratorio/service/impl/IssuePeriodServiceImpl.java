package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.FluxColumn;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.Issue;
import br.com.jiratorio.domain.entity.IssuePeriod;
import br.com.jiratorio.domain.form.IssuePeriodForm;
import br.com.jiratorio.domain.ChartAggregator;
import br.com.jiratorio.domain.IssueCountBySize;
import br.com.jiratorio.domain.IssuePeriodChart;
import br.com.jiratorio.domain.IssuePeriodDetails;
import br.com.jiratorio.domain.IssuePeriodList;
import br.com.jiratorio.domain.LeadTimeCompareChart;
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
    public Long create(final IssuePeriodForm issuePeriodForm, final Long boardId) {
        log.info("Method=create, issuePeriodForm={}, boardId={}", issuePeriodForm, boardId);

        delete(issuePeriodForm, boardId);

        Board board = boardService.findById(boardId);

        String jql = jqlService.finalizedIssues(board, issuePeriodForm.getStartDate(), issuePeriodForm.getEndDate());
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
        Double wipAvg = wipService.calcAvgWip(issuePeriodForm.getStartDate(), issuePeriodForm.getEndDate(),
                issues, fluxColumn.getStartColumns());

        IssuePeriodDetails details = IssuePeriodDetails.builder()
                .boardId(boardId)
                .jql(jql)
                .wipAvg(wipAvg)
                .avgLeadTime(avgLeadTime)
                .issueCount(issues.size())
                .avgPctEfficiency(avgPctEfficiency)
                .build();

        IssuePeriod issuePeriod = issuePeriodMapper.fromJiraData(issuePeriodForm, issues,
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
        IssuePeriodForm issuePeriodForm = new IssuePeriodForm(issuePeriod.getStartDate(), issuePeriod.getEndDate());

        create(issuePeriodForm, issuePeriod.getBoardId());
    }

    @Override
    @Transactional(readOnly = true)
    public IssuePeriodList findIssuePeriodsAndCharts(final Long boardId) {
        Board board = boardService.findById(boardId);
        List<IssuePeriod> issuePeriods = findByBoardId(boardId);
        IssuePeriodChart issuePeriodChart = buildCharts(issuePeriods, board);

        return IssuePeriodList.builder()
                .issuePeriods(issuePeriods)
                .issuePeriodChart(issuePeriodChart)
                .build();
    }

    private void delete(final IssuePeriodForm issuePeriodForm, final Long boardId) {
        log.info("Method=delete, issuePeriodForm={}, boardId={}", issuePeriodForm, boardId);

        IssuePeriod issuePeriod = issuePeriodRepository.findByStartDateAndEndDateAndBoardId(issuePeriodForm.getStartDate(),
                issuePeriodForm.getEndDate(), boardId);

        if (issuePeriod != null) {
            delete(issuePeriod);
        }
    }

    private void delete(final IssuePeriod issuePeriod) {
        issuePeriodRepository.delete(issuePeriod);
        issueService.deleteAll(issuePeriod.getIssues());

    }
}
