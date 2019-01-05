package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.embedded.Histogram;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.DynamicChart;
import br.com.leonardoferreira.jirareport.domain.vo.IssueCountBySize;
import br.com.leonardoferreira.jirareport.domain.vo.LeadTimeCompareChart;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ChartService {

    CompletableFuture<Histogram> issueHistogram(List<Issue> issues);

    CompletableFuture<Chart<String, Long>> estimatedChart(List<Issue> issues);

    CompletableFuture<Chart<String, Double>> leadTimeBySystem(List<Issue> issues);

    CompletableFuture<Chart<String, Long>> tasksBySystem(List<Issue> issues);

    CompletableFuture<Chart<String, Double>> leadTimeBySize(List<Issue> issues);

    CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg(List<Issue> issues, List<String> fluxColumn);

    CompletableFuture<Chart<String, Double>> leadTimeByType(List<Issue> issues);

    CompletableFuture<Chart<String, Long>> tasksByType(List<Issue> issues);

    CompletableFuture<Chart<String, Double>> leadTimeByProject(List<Issue> issues);

    CompletableFuture<Chart<String, Long>> tasksByProject(List<Issue> issues);

    ChartAggregator buildAllCharts(List<Issue> issues, Board board);

    CompletableFuture<Chart<String, Double>> calcLeadTimeCompare(List<Issue> issues);

    LeadTimeCompareChart calcLeadTimeCompareByPeriod(List<IssuePeriod> issuePeriods, Board board);

    IssueCountBySize buildIssueCountBySize(List<IssuePeriod> issuePeriods);

    CompletableFuture<Chart<String, Double>> leadTimeByPriority(List<Issue> issues);

    CompletableFuture<Chart<String, Long>> throughputByPriority(List<Issue> issues);

    CompletableFuture<List<DynamicChart>> buildDynamicCharts(Board board, List<Issue> issues);
}
