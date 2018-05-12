package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.embedded.LeadTimeBySize;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.service.ChartService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> estimatedChart(final List<Issue> issues) {
        log.info("Method=estimatedChart, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> i.getEstimated() != null)
                .collect(Collectors.groupingBy(Issue::getEstimated, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Double>> leadTimeBySystem(final List<Issue> issues) {
        log.info("Method=leadTimeBySystem, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getSystem()) && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getSystem, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> tasksBySystem(final List<Issue> issues) {
        log.info("Method=tasksBySystem, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getSystem()))
                .collect(Collectors.groupingBy(Issue::getSystem, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Double>> leadTimeBySize(final List<Issue> issues) {
        log.info("Method=leadTimeBySize, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getEstimated()) && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getEstimated, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    @Override
    public CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg(final List<Issue> issues) {
        log.info("Method=columnTimeAvg, issues={}", issues);

        List<ColumnTimeAvg> collect = new ArrayList<>();
        issues.stream()
                .map(Issue::getChangelog)
                .flatMap(Collection::stream)
                .filter(changelog -> changelog.getTo() != null && changelog.getCycleTime() != null)
                .collect(Collectors.groupingBy(Changelog::getTo, Collectors.averagingDouble(Changelog::getCycleTime)))
                .forEach((k, v)-> collect.add(new ColumnTimeAvg(k, v)));

        return CompletableFuture.completedFuture(collect);
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Double>> leadTimeByType(final List<Issue> issues) {
        log.info("Method=leadTimeByType, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getIssueType()) && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getIssueType, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> tasksByType(final List<Issue> issues) {
        Map<String, Long> collect = issues.stream()
                .filter(i -> !StringUtils.isEmpty(i.getIssueType()))
                .collect(Collectors.groupingBy(Issue::getIssueType, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Override
    @SneakyThrows
    public ChartAggregator buildAllCharts(final List<Issue> issues) {
        CompletableFuture<Chart<Long, Long>> histogram = chartService.issueHistogram(issues);
        CompletableFuture<Chart<String, Long>> estimated = chartService.estimatedChart(issues);
        CompletableFuture<Chart<String, Double>> leadTimeBySystem = chartService.leadTimeBySystem(issues);
        CompletableFuture<Chart<String, Long>> tasksBySystem = chartService.tasksBySystem(issues);
        CompletableFuture<Chart<String, Double>> leadTimeBySize = chartService.leadTimeBySize(issues);
        CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg = chartService.columnTimeAvg(issues);
        CompletableFuture<Chart<String, Double>> leadTimeByType = chartService.leadTimeByType(issues);
        CompletableFuture<Chart<String, Long>> tasksByType = chartService.tasksByType(issues);

        return new ChartAggregator(histogram.get(), estimated.get(), leadTimeBySystem.get(), tasksBySystem.get(),
                leadTimeBySize.get(), columnTimeAvg.get(), leadTimeByType.get(), tasksByType.get());
    }
}
