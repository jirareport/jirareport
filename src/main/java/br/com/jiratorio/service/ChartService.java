package br.com.jiratorio.service;

import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.Issue;
import br.com.jiratorio.domain.IssuePeriod;
import br.com.jiratorio.domain.embedded.Chart;
import br.com.jiratorio.domain.embedded.ColumnTimeAvg;
import br.com.jiratorio.domain.embedded.Histogram;
import br.com.jiratorio.domain.vo.ChartAggregator;
import br.com.jiratorio.domain.vo.DynamicChart;
import br.com.jiratorio.domain.vo.IssueCountBySize;
import br.com.jiratorio.domain.vo.LeadTimeCompareChart;

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
