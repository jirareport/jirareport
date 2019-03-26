package br.com.jiratorio.domain;

import br.com.jiratorio.domain.chart.LeadTimeCompareChart;
import br.com.jiratorio.domain.entity.embedded.Chart;
import br.com.jiratorio.domain.entity.IssuePeriod;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssuePeriodChart {

    private Chart<String, String> leadtime;

    private Chart<String, Integer> issuesCount;

    private IssueCountBySize issueCountBySize;

    private LeadTimeCompareChart leadTimeCompareChart;

    public IssuePeriodChart() {
        leadtime = new Chart<>();
        issuesCount = new Chart<>();
    }

    public void addLeadTime(final IssuePeriod issuePeriod) {
        leadtime.add(issuePeriod.getDates(), String.format("%.2f", issuePeriod.getAvgLeadTime()));
    }

    public void addIssuesCount(final IssuePeriod issuePeriod) {
        issuesCount.add(issuePeriod.getDates(), issuePeriod.getIssuesCount());
    }

}
