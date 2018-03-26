package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.LeadtimePrediction;
import br.com.leonardoferreira.jirareport.domain.vo.ChartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author leferreira
 * @since 3/20/18 9:52 AM
 */
@Slf4j
@Service
public class ChartService {

    @Async
    public CompletableFuture<ChartVO<Long, Long>> issueHistogram(final List<Issue> issues) {
        log.info("Method=issueHistogram, issues={}", issues);

        Map<Long, Long> collect = issues.stream()
                .filter(i -> i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getLeadTime, Collectors.counting()));

        Long max = collect.keySet().stream().max(Long::compare).orElse(1L);

        for (long i = 1; i < max; i++) {
            collect.putIfAbsent(i, 0L);
        }

        return CompletableFuture.completedFuture(new ChartVO<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    public CompletableFuture<ChartVO<String, Long>> estimatedChart(final List<Issue> issues) {
        log.info("Method=estimatedChart, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> i.getEstimated() != null)
                .collect(Collectors.groupingBy(Issue::getEstimated, Collectors.counting()));

        return CompletableFuture.completedFuture(new ChartVO<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    public CompletableFuture<ChartVO<String, Double>> leadTimeBySystem(final List<Issue> issues) {
        log.info("Method=leadTimeBySystem, issues={}", issues);

        Map<String, Double> collect = issues.stream()
                .filter(i -> !i.getComponents().isEmpty() && i.getLeadTime() != null)
                .collect(Collectors.groupingBy(Issue::getComponentsStr, Collectors.averagingLong(Issue::getLeadTime)));

        return CompletableFuture.completedFuture(new ChartVO<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    public CompletableFuture<ChartVO<String, Long>> tasksBySystem(final List<Issue> issues) {
        log.info("Method=tasksBySystem, issues={}", issues);

        Map<String, Long> collect = issues.stream()
                .filter(i -> !i.getComponents().isEmpty())
                .collect(Collectors.groupingBy(Issue::getComponentsStr, Collectors.counting()));

        return CompletableFuture.completedFuture(new ChartVO<>(new ArrayList<>(collect.keySet()), new ArrayList<>(collect.values())));
    }

    @Async
    public CompletableFuture<List<LeadtimePrediction>> predictionChart(final List<Issue> issues) {
        log.info("Method=predictionChart, issues={}", issues);
        final List<LeadtimePrediction> collect = new ArrayList<>();
        issues.stream()
                .filter(i -> i.getLeadTime() != null && i.getEstimated() != null)
                .collect(Collectors.groupingBy(Issue::getEstimated, Collectors.averagingDouble(Issue::getLeadTime)))
                .forEach((k, v) -> collect.add(new LeadtimePrediction(k, v)));

        return CompletableFuture.completedFuture(collect);
    }
}
