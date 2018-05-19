package br.com.leonardoferreira.jirareport.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.LeadTime;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.IssueCountBySize;
import br.com.leonardoferreira.jirareport.domain.vo.LeadTimeCompareChart;
import br.com.leonardoferreira.jirareport.service.ChartService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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
    public CompletableFuture<Chart<Long, Long>> issueHistogram(final List<Issue> issues) {
        log.info("Method=issueHistogram, issues={}", issues);

        Map<Long, Long> collect = issues.stream()
                .filter(i -> i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getLeadTime, Collectors.counting()));

        Long max = collect.keySet().stream().max(Long::compare).orElse(1L);

        for (long i = 1; i < max; i++) {
            collect.putIfAbsent(i, 0L);
        }

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> estimatedChart(final List<Issue> issues) {
        log.info("Method=estimatedChart, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> i.getEstimated() != null)
                .collect(Collectors.groupingBy(Issue::getEstimated, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Double>> leadTimeBySystem(final List<Issue> issues) {
        log.info("Method=leadTimeBySystem, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getSystem()) && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getSystem, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> tasksBySystem(final List<Issue> issues) {
        log.info("Method=tasksBySystem, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getSystem()))
                .collect(Collectors.groupingBy(Issue::getSystem, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Double>> leadTimeBySize(final List<Issue> issues) {
        log.info("Method=leadTimeBySize, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getEstimated()) && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getEstimated, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg(final List<Issue> issues) {
        log.info("Method=columnTimeAvg, issues={}", issues);

        List<ColumnTimeAvg> collect = new ArrayList<>();
        issues.stream()
                .map(Issue::getChangelog)
                .flatMap(Collection::stream)
                .filter(changelog -> changelog.getTo() != null && changelog.getLeadTime() != null)
                .collect(Collectors.groupingBy(Changelog::getTo, Collectors.averagingDouble(Changelog::getLeadTime)))
                .forEach((k, v) -> collect.add(new ColumnTimeAvg(k, v)));

        return CompletableFuture.completedFuture(collect);
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Double>> leadTimeByType(final List<Issue> issues) {
        log.info("Method=leadTimeByType, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getIssueType()) && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getIssueType, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> tasksByType(final List<Issue> issues) {
        log.info("Method=tasksByType, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getIssueType()))
                .collect(Collectors.groupingBy(Issue::getIssueType, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Double>> leadTimeByProject(final List<Issue> issues) {
        log.info("Method=leadTimeByProject, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getProject()) && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getProject, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> tasksByProject(final List<Issue> issues) {
        log.info("Method=tasksByProject, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getProject()))
                .collect(Collectors.groupingBy(Issue::getProject, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(collect));
    }

    @Override
    @SneakyThrows
    public ChartAggregator buildAllCharts(final List<Issue> issues) {
        log.info("Method=buildAllCharts, issues={}", issues);

        CompletableFuture<Chart<Long, Long>> histogram = chartService.issueHistogram(issues);
        CompletableFuture<Chart<String, Long>> estimated = chartService.estimatedChart(issues);
        CompletableFuture<Chart<String, Double>> leadTimeBySystem = chartService.leadTimeBySystem(issues);
        CompletableFuture<Chart<String, Long>> tasksBySystem = chartService.tasksBySystem(issues);
        CompletableFuture<Chart<String, Double>> leadTimeBySize = chartService.leadTimeBySize(issues);
        CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg = chartService.columnTimeAvg(issues);
        CompletableFuture<Chart<String, Double>> leadTimeByType = chartService.leadTimeByType(issues);
        CompletableFuture<Chart<String, Long>> tasksByType = chartService.tasksByType(issues);
        CompletableFuture<Chart<String, Double>> leadTimeByProject = chartService.leadTimeByProject(issues);
        CompletableFuture<Chart<String, Long>> tasksByProject = chartService.tasksByProject(issues);

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
                .build();
    }

    @Override
    public LeadTimeCompareChart<Long> calcLeadTimeCompare(final List<Issue> issues) {
        log.info("Method=calcLeadTimeCompare, issues={}", issues);

        final LeadTimeCompareChart<Long> chart = new LeadTimeCompareChart<>();
        for (Issue issue : issues) {
            final Map<String, Long> collect = new HashMap<>();
            for (LeadTime leadTime : issue.getLeadTimes()) {
                collect.put(leadTime.getLeadTimeConfig().getName(), leadTime.getLeadTime());
            }
            chart.add(issue.getKey(), collect);
        }

        return chart;
    }

    @Override
    @Transactional(readOnly = true)
    public LeadTimeCompareChart<Double> calcLeadTimeCompareByPeriod(final List<IssuePeriod> issuePeriods) {
        log.info("Method=calcLeadTimeCompareByPeriod, issuePeriods={}", issuePeriods);
        LeadTimeCompareChart<Double> leadTimeCompareChart = new LeadTimeCompareChart<>();

        for (IssuePeriod issuePeriod : issuePeriods) {
            Map<String, Double> collect = issuePeriod.getIssues()
                    .stream()
                    .map(Issue::getLeadTimes)
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(i -> i.getLeadTimeConfig().getName(),
                            Collectors.averagingDouble(LeadTime::getLeadTime)));
            leadTimeCompareChart.add(issuePeriod.getId().getDates(), collect);
        }

        return leadTimeCompareChart;
    }

    @Override
    @Transactional(readOnly = true)
    public IssueCountBySize buildIssueCountBySize(final List<IssuePeriod> issuePeriods) {
        log.info("Method=buildIssueCountBySize, issuePeriods={}", issuePeriods);

        Set<String> sizes = new HashSet<>();
        Map<String, Map<String, Long>> periodsSize = new HashMap<>();
        for (IssuePeriod issuePeriod : issuePeriods) {
            Map<String, Long> estimated = issuePeriod.getEstimated().getData();
            sizes.addAll(issuePeriod.getEstimated().getData().keySet());

            periodsSize.put(issuePeriod.getId().getDates(), estimated);
        }

        periodsSize.forEach((k, v) -> {
            for (String size : sizes) {
                if (!v.containsKey(size)) {
                    v.put(size, 0L);
                }
            }
        });

        Map<String, List<Long>> datasources = new HashMap<>();
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

}
