package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.LeadtimePrediction;
import br.com.leonardoferreira.jirareport.domain.vo.ChartVO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by lferreira on 3/26/18
 */
public interface ChartService {

    CompletableFuture<ChartVO<Long, Long>> issueHistogram(List<Issue> issues);

    CompletableFuture<ChartVO<String, Long>> estimatedChart(List<Issue> issues);

    CompletableFuture<ChartVO<String, Double>> leadTimeBySystem(List<Issue> issues);

    CompletableFuture<ChartVO<String, Long>> tasksBySystem(List<Issue> issues);

    CompletableFuture<List<LeadtimePrediction>> predictionChart(List<Issue> issues);

}
