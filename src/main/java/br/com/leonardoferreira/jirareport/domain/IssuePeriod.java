package br.com.leonardoferreira.jirareport.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.embedded.LeadTimeBySize;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * @author lferreira
 * @since 7/28/17 1:44 PM
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IssuePeriod extends BaseEntity {
    private static final long serialVersionUID = 7188140641247774389L;

    @EmbeddedId
    private IssuePeriodId form;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "issue_period_issue",
            joinColumns = {
                    @JoinColumn(name = "issue_period_project_id"),
                    @JoinColumn(name = "issue_period_start_date"),
                    @JoinColumn(name = "issue_period_end_date")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "issue_id")
            }
    )
    private List<Issue> issues;

    private Double avgLeadTime;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<Long, Long> histogram;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Long> estimated;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Double> leadTimeBySystem;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Long> tasksBySystem;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<LeadTimeBySize> leadTimeBySize;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<ColumnTimeAvg> columnTimeAvgs;

    @Transient
    public Integer getIssuesCount() {
        return issues.size();
    }

    @Transient
    public String getLeadTime() {
        return String.format("%.2f", avgLeadTime);
    }
}
