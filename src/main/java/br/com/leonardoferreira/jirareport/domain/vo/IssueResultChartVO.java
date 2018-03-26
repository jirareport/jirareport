package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.IssueResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author leferreira
 * @since 11/23/17 12:39 PM
 */
@Data
@AllArgsConstructor
public class IssueResultChartVO {
    private ChartVO<String, String> leadtime;
    private ChartVO<String, Integer> issuesCount;

    public IssueResultChartVO() {
        leadtime = new ChartVO<>();
        issuesCount = new ChartVO<>();
    }

    public void addLeadTime(IssueResult issueResult) {
        leadtime.add(issueResult.getForm().getDates(), issueResult.getLeadTime());
    }

    public void addIssuesCount(final IssueResult issueResult) {
        issuesCount.add(issueResult.getForm().getDates(), issueResult.getIssuesCount());
    }
}
