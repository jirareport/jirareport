package br.com.leonardoferreira.jirareport.domain;

import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author lferreira
 * @since 7/28/17 1:44 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuePeriod {

    @Id
    @Indexed(unique = true)
    private IssueForm form;

    @DBRef
    private List<Issue> issues;

    private Double avgLeadTime;

    private ChartVO<Long, Long> histogram;

    private ChartVO<String, Long> estimated;

    private ChartVO<String, Double> leadTimeBySystem;

    private ChartVO<String, Long> tasksBySystem;

    private List<LeadTimeBySize> leadTimeBySize;

    public Integer getIssuesCount() {
        return issues.size();
    }

    public String getLeadTime() {
        return String.format("%.2f", avgLeadTime);
    }
}
