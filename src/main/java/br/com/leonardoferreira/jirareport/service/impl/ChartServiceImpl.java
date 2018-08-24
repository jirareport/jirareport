package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.LeadTime;
import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.embedded.Histogram;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.IssueCountBySize;
import br.com.leonardoferreira.jirareport.domain.vo.LeadTimeCompareChart;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author lferreira
 * @since 3/20/18 9:52 AM
 */
@Slf4j
@Service
public class ChartServiceImpl extends AbstractService implements ChartService {

    @Autowired
    private ChartService chartService;

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Histogram> issueHistogram(final List<Issue> issues) {
        log.info("Method=issueHistogram, issues={}", issues);

        issues.sort(Comparator.comparing(Issue::getLeadTime));

        int medianIndex = calculateCeilingPercentage(issues.size(), 50);
        int percentile75Index = calculateCeilingPercentage(issues.size(), 75);
        int percentile90Index = calculateCeilingPercentage(issues.size(), 90);

        Long median = issues.get(medianIndex - 1).getLeadTime();
        Long percentile75 = issues.get(percentile75Index - 1).getLeadTime();
        Long percentile90 = issues.get(percentile90Index - 1).getLeadTime();

        Chart<Long, Long> chart = histogramChart(issues);

        Histogram histogram = Histogram.builder()
                .chart(chart)
                .median(median)
                .percentile75(percentile75)
                .percentile90(percentile90)
                .build();

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
                .filter(i -> i.getLeadTime() != null)
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
                .filter(i -> i.getLeadTime() != null)
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
                .filter(changelog -> changelog.getTo() != null && changelog.getLeadTime() != null)
                .collect(Collectors.groupingBy(Changelog::getTo, Collectors.summingDouble(Changelog::getLeadTime)))
                .forEach((k, v) -> collect.add(new ColumnTimeAvg(k, v / issues.size())));

        if (fluxColumn != null) {
            collect.sort(Comparator.comparingInt(i -> fluxColumn.indexOf(i.getColumnName().toUpperCase(DateUtil.LOCALE_BR))));
        }

        return CompletableFuture.completedFuture(collect);
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Double>> leadTimeByType(final List<Issue> issues) {
        log.info("Method=leadTimeByType, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getIssueType()) && i.getLeadTime() != null)
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
                .filter(i -> i.getLeadTime() != null)
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

        return ChartAggregator.builder()
                .histogram(histogram.get())
                .estimated(estimated.get())
                .leadTimeBySystem(leadTimeBySystem.get())
                .tasksBySystem(tasksBySystem.get())
                .leadTimeBySize(leadTimeBySize.get())
                .columnTimeAvg(columnTimeAvg.get())
                .leadTimeByType(leadTimeByType.get())
                .tasksByType(tasksByType.get())
                .leadTimeByProject(leadTimeByProject.get())
                .tasksByProject(tasksByProject.get())
                .leadTimeCompareChart(leadTimeCompareChart.get())
                .build();
    }

    @Async
    @Override
    @ExecutionTime
    public CompletableFuture<Chart<String, Double>> calcLeadTimeCompare(final List<Issue> issues) {
        log.info("Method=calcLeadTimeCompare, issues={}", issues);

        Map<String, Double> collect = issues.stream()
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

        return IssueCountBySize.builder()
                .labels(periodsSize.keySet())
                .datasources(datasources)
                .build();
    }

    private int calculateCeilingPercentage(final int totalElements, final int percentage) {
        return new BigDecimal((double) totalElements * percentage / 100).setScale(0, RoundingMode.CEILING)
                .intValue();
    }

    private Chart<Long, Long> histogramChart(final List<Issue> issues) {
        Map<Long, Long> collect = issues.stream()
                .filter(i -> i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getLeadTime, Collectors.counting()));

        Long max = collect.keySet().stream().max(Long::compare).orElse(1L);

        for (long i = 1; i < max; i++) {
            collect.putIfAbsent(i, 0L);
        }

        return new Chart<>(collect);
    }
}
