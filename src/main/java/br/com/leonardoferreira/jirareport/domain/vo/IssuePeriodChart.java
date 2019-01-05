package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
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
