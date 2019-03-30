package br.com.jiratorio.service.impl;

import br.com.jiratorio.aspect.annotation.ExecutionTime;
import br.com.jiratorio.domain.Percentile;
import br.com.jiratorio.domain.chart.ChartAggregator;
import br.com.jiratorio.domain.chart.IssueCountBySize;
import br.com.jiratorio.domain.chart.LeadTimeCompareChart;
import br.com.jiratorio.domain.dynamicfield.DynamicChart;
import br.com.jiratorio.domain.dynamicfield.DynamicFieldConfig;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.Issue;
import br.com.jiratorio.domain.entity.IssuePeriod;
import br.com.jiratorio.domain.entity.LeadTime;
import br.com.jiratorio.domain.entity.LeadTimeConfig;
import br.com.jiratorio.domain.entity.embedded.Changelog;
import br.com.jiratorio.domain.entity.embedded.Chart;
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg;
import br.com.jiratorio.domain.entity.embedded.Histogram;
import br.com.jiratorio.service.ChartService;
import br.com.jiratorio.service.PercentileService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    private ChartService chartService;

    private final PercentileService percentileService;

    public ChartServiceImpl(final PercentileService percentileService) {
        this.percentileService = percentileService;
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Histogram> issueHistogram(final List<Issue> issues) {
        log.info("Method=issueHistogram, issues={}", issues);

        List<Long> leadTimeList = issues.stream().map(Issue::getLeadTime).collect(Collectors.toList());
        Percentile percentile = percentileService.calculatePercentile(leadTimeList);
        Chart<Long, Long> chart = histogramChart(issues);

        Histogram histogram = new Histogram(chart, percentile.getMedian(), percentile.getPercentile75(), percentile.getPercentile90());

        return CompletableFuture.completedFuture(histogram);
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Long>> estimatedChart(final List<Issue> issues) {
        log.info("Method=estimatedChart, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getEstimated()).orElse("Não informado"),
                        Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Double>> leadTimeBySystem(final List<Issue> issues) {
        log.info("Method=leadTimeBySystem, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getSystem()).orElse("Não informado"),
                        Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Long>> tasksBySystem(final List<Issue> issues) {
        log.info("Method=tasksBySystem, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getSystem()).orElse("Não informado"),
                        Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Double>> leadTimeBySize(final List<Issue> issues) {
        log.info("Method=leadTimeBySize, issues={}", issues);

        Map<String, Double> collect = issues.stream()

                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getEstimated()).orElse("Não informado"),
                        Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg(final List<Issue> issues, final List<String> fluxColumn) {
        log.info("Method=columnTimeAvg, issues={}", issues);

        List<ColumnTimeAvg> collect = new ArrayList<>();
        issues.stream()
                .map(Issue::getChangelog)
                .flatMap(Collection::stream)
                .filter(changelog -> changelog.getTo() != null)
                .collect(Collectors.groupingBy(Changelog::getTo, Collectors.summingDouble(Changelog::getLeadTime)))
                .forEach((k, v) -> collect.add(new ColumnTimeAvg(k, v / issues.size())));

        if (fluxColumn != null) {
            collect.sort(Comparator.comparingInt(i -> fluxColumn.indexOf(i.getColumnName().toUpperCase())));
        }

        return CompletableFuture.completedFuture(collect);
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Double>> leadTimeByType(final List<Issue> issues) {
        log.info("Method=leadTimeByType, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getIssueType()))
                .collect(Collectors.groupingBy(Issue::getIssueType, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Long>> tasksByType(final List<Issue> issues) {
        log.info("Method=tasksByType, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getIssueType()))
                .collect(Collectors.groupingBy(Issue::getIssueType, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Double>> leadTimeByProject(final List<Issue> issues) {
        log.info("Method=leadTimeByProject, issues={}", issues);

        Map<String, Double> collect = issues.stream()

                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getProject()).orElse("Não informado"),
                        Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Long>> tasksByProject(final List<Issue> issues) {
        log.info("Method=tasksByProject, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getProject()).orElse("Não informado"),
                        Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Override
    @SneakyThrows
    @ExecutionTime
    public ChartAggregator buildAllCharts(final List<Issue> issues, final Board board) {
        log.info("Method=buildAllCharts, issues={}", issues);

        CompletableFuture<Histogram> histogram = chartService.issueHistogram(issues);
        CompletableFuture<Chart<String, Long>> estimated = chartService.estimatedChart(issues);
        CompletableFuture<Chart<String, Double>> leadTimeBySystem = chartService.leadTimeBySystem(issues);
        CompletableFuture<Chart<String, Long>> tasksBySystem = chartService.tasksBySystem(issues);
        CompletableFuture<Chart<String, Double>> leadTimeBySize = chartService.leadTimeBySize(issues);
        CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg = chartService.columnTimeAvg(issues, board.getFluxColumn());
        CompletableFuture<Chart<String, Double>> leadTimeByType = chartService.leadTimeByType(issues);
        CompletableFuture<Chart<String, Long>> tasksByType = chartService.tasksByType(issues);
        CompletableFuture<Chart<String, Double>> leadTimeByProject = chartService.leadTimeByProject(issues);
        CompletableFuture<Chart<String, Long>> tasksByProject = chartService.tasksByProject(issues);
        CompletableFuture<Chart<String, Double>> leadTimeCompareChart = chartService.calcLeadTimeCompare(issues);
        CompletableFuture<Chart<String, Double>> leadTimeByPriority = chartService.leadTimeByPriority(issues);
        CompletableFuture<Chart<String, Long>> throughputByPriority = chartService.throughputByPriority(issues);
        CompletableFuture<List<DynamicChart>> dynamicCharts = chartService.buildDynamicCharts(board, issues);

        return new ChartAggregator(
                histogram.get(),
                estimated.get(),
                leadTimeBySystem.get(),
                tasksBySystem.get(),
                leadTimeBySize.get(),
                columnTimeAvg.get(),
                leadTimeByType.get(),
                tasksByType.get(),
                leadTimeByProject.get(),
                tasksByProject.get(),
                leadTimeCompareChart.get(),
                leadTimeByPriority.get(),
                throughputByPriority.get(),
                dynamicCharts.get()
        );
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Double>> calcLeadTimeCompare(final List<Issue> issues) {
        log.info("Method=calcLeadTimeCompare, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(it -> !CollectionUtils.isEmpty(it.getLeadTimes()))
                .map(Issue::getLeadTimes)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(i -> i.getLeadTimeConfig().getName(),
                        Collectors.averagingDouble(LeadTime::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public LeadTimeCompareChart calcLeadTimeCompareByPeriod(final List<IssuePeriod> issuePeriods, final Board board) {
        log.info("Method=calcLeadTimeCompareByPeriod, issuePeriods={}", issuePeriods);
        LeadTimeCompareChart leadTimeCompareChart = new LeadTimeCompareChart();

        for (IssuePeriod issuePeriod : issuePeriods) {
            Map<String, Double> collect = issuePeriod.getIssues()
                    .stream()
                    .map(Issue::getLeadTimes)
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(i -> i.getLeadTimeConfig().getName(),
                            Collectors.averagingDouble(LeadTime::getLeadTime)));

            if (collect.size() < board.getLeadTimeConfigs().size()) {
                for (LeadTimeConfig leadTimeConfig : board.getLeadTimeConfigs()) {
                    if (!collect.containsKey(leadTimeConfig.getName())) {
                        collect.put(leadTimeConfig.getName(), 0D);
                    }
                }
            }

            leadTimeCompareChart.add(issuePeriod.getDates(), collect);
        }

        return leadTimeCompareChart;
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public IssueCountBySize buildIssueCountBySize(final List<IssuePeriod> issuePeriods) {
        log.info("Method=buildIssueCountBySize, issuePeriods={}", issuePeriods);

        Set<String> sizes = new HashSet<>();
        Map<String, Map<String, Long>> periodsSize = new LinkedHashMap<>();
        for (IssuePeriod issuePeriod : issuePeriods) {
            Map<String, Long> estimated = issuePeriod.getEstimated().getData();
            sizes.addAll(issuePeriod.getEstimated().getData().keySet());

            periodsSize.put(issuePeriod.getDates(), estimated);
        }

        periodsSize.forEach((k, v) -> {
            for (String size : sizes) {
                if (!v.containsKey(size)) {
                    v.put(size, 0L);
                }
            }
        });

        Map<String, List<Long>> datasources = new LinkedHashMap<>();
        for (Map<String, Long> periodSize : periodsSize.values()) {
            periodSize.forEach((k, v) -> {
                if (datasources.containsKey(k)) {
                    List<Long> longs = datasources.get(k);
                    longs.add(v);
                    datasources.put(k, longs);
                } else {
                    ArrayList<Long> value = new ArrayList<>();
                    value.add(v);
                    datasources.put(k, value);
                }
            });
        }

        return new IssueCountBySize(periodsSize.keySet(), datasources);
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Double>> leadTimeByPriority(final List<Issue> issues) {
        log.info("Method=leadTimeByPriority, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getPriority()))
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getPriority()).orElse("Não informado"),
                        Collectors.averagingDouble(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> throughputByPriority(final List<Issue> issues) {
        log.info("Method=throughputByPriority, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getPriority()))
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getPriority()).orElse("Não informado"),
                        Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<List<DynamicChart>> buildDynamicCharts(final Board board, final List<Issue> issues) {
        log.info("Method=buildDynamicCharts, board={}, issues={}", board, issues);

        if (board.getDynamicFields() == null || board.getDynamicFields().isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        List<DynamicChart> collect = board.getDynamicFields().stream()
                .map(config -> {
                    Chart<String, Double> leadTime = buildDynamicLeadTime(config, issues);
                    Chart<String, Long> throughput = buildDynamicThroughput(config, issues);
                    return new DynamicChart(config.getName(), leadTime, throughput);
                })
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(collect);
    }

    private Chart<String, Long> buildDynamicThroughput(final DynamicFieldConfig config, final List<Issue> issues) {
        Map<String, Long> collect = issues.stream()
                .filter(i -> !CollectionUtils.isEmpty(i.getDynamicFields()))
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getDynamicFields().get(config.getName())).orElse("Não informado"),
                        Collectors.counting()));

        return new Chart<>(collect);
    }

    private Chart<String, Double> buildDynamicLeadTime(final DynamicFieldConfig config, final List<Issue> issues) {
        Map<String, Double> collect = issues.stream()
                .filter(i -> !CollectionUtils.isEmpty(i.getDynamicFields()))
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getDynamicFields().get(config.getName())).orElse("Não informado"),
                        Collectors.averagingDouble(Issue::getLeadTime)));
        return new Chart<>(collect);
    }

    private Chart<Long, Long> histogramChart(final List<Issue> issues) {
        Map<Long, Long> collect = issues.stream()

                .collect(Collectors.groupingBy(Issue::getLeadTime, Collectors.counting()));

        Long max = collect.keySet().stream().max(Long::compare).orElse(1L);

        for (long i = 1; i < max; i++) {
            collect.putIfAbsent(i, 0L);
        }

        return new Chart<>(collect);
    }
}
