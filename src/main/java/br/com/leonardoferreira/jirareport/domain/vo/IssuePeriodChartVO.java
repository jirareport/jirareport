package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lferreira
 * @since 11/23/17 12:39 PM
 */
@Data
@AllArgsConstructor
public class IssuePeriodChartVO {

    private ChartVO<String, String> leadtime;

    private ChartVO<String, Integer> issuesCount;

    public IssuePeriodChartVO() {
        leadtime = new ChartVO<>();
        issuesCount = new ChartVO<>();
    }

    public void addLeadTime(IssuePeriod issuePeriod) {
        leadtime.add(issuePeriod.getForm().getDates(), issuePeriod.getLeadTime());
    }

    public void addIssuesCount(final IssuePeriod issuePeriod) {
        issuesCount.add(issuePeriod.getForm().getDates(), issuePeriod.getIssuesCount());
    }
}
