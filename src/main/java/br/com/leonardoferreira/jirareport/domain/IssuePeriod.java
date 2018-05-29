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
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

/**
 * @author lferreira
 * @since 7/28/17 1:44 PM
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "issues" })
@EqualsAndHashCode(callSuper = false)
public class IssuePeriod extends BaseEntity {

    private static final long serialVersionUID = 7188140641247774389L;

    @EmbeddedId
    private IssuePeriodId id;

    @OrderBy("key asc")
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(
            name = "issue_period_issue",
            joinColumns = {
                    @JoinColumn(name = "end_date"),
                    @JoinColumn(name = "project_id"),
                    @JoinColumn(name = "start_date")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "issue_key")
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
    private Chart<String, Double> leadTimeBySize;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<ColumnTimeAvg> columnTimeAvgs;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Double> leadTimeByType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Long> tasksByType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Double> leadTimeByProject;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Long> tasksByProject;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Double> leadTimeCompareChart;

    @Formula("( select count(1) from issue_period_issue ipi "
            + " where ipi.project_id = project_id "
            + " and ipi.start_date = start_date"
            + " and ipi.end_date = end_date )")
    private Integer issuesCount;

    @Transient
    public String getLeadTime() {
        return String.format("%.2f", avgLeadTime);
    }
}
