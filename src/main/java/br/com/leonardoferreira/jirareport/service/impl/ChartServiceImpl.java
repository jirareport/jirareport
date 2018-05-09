package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.embedded.LeadTimeBySize;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author lferreira
 * @since 3/20/18 9:52 AM
 */
@Slf4j
@Service
public class ChartServiceImpl extends AbstractService implements ChartService {

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
                .filter(i -> !i.getComponents().isEmpty() && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getComponentsStr, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    @Override
    public CompletableFuture<Chart<String, Long>> tasksBySystem(final List<Issue> issues) {
        log.info("Method=tasksBySystem, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> !i.getComponents().isEmpty())
                .collect(Collectors.groupingBy(Issue::getComponentsStr, Collectors.counting()));

        return CompletableFuture.completedFuture(new Chart<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    @Override
    public CompletableFuture<List<LeadTimeBySize>> leadTimeBySize(final List<Issue> issues) {
        log.info("Method=leadTimeBySize, issues={}", issues);
        final List<LeadTimeBySize> collect = new ArrayList<>();
        issues.stream()
                .filter(i -> i.getLeadTime() != null && i.getEstimated() != null)
                .collect(Collectors.groupingBy(Issue::getEstimated, Collectors.averagingDouble(Issue::getLeadTime)))
                .forEach((k, v) -> collect.add(new LeadTimeBySize(k, v)));

        return CompletableFuture.completedFuture(collect);
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
}
