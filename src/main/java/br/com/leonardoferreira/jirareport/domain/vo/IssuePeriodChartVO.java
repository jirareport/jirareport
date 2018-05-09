package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lferreira
 * @since 11/23/17 12:39 PM
 */
@Data
@AllArgsConstructor
public class IssuePeriodChartVO {

    private Chart<String, String> leadtime;

    private Chart<String, Integer> issuesCount;

    public IssuePeriodChartVO() {
        leadtime = new Chart<>();
        issuesCount = new Chart<>();
    }

    public void addLeadTime(IssuePeriod issuePeriod) {
        leadtime.add(issuePeriod.getForm().getDates(), issuePeriod.getLeadTime());
    }

    public void addIssuesCount(final IssuePeriod issuePeriod) {
        issuesCount.add(issuePeriod.getForm().getDates(), issuePeriod.getIssuesCount());
    }
}
