package br.com.leonardoferreira.jirareport.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.embedded.Histogram;
import br.com.leonardoferreira.jirareport.domain.vo.DynamicChart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long boardId;

    @OrderBy("key asc")
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(
            name = "issue_period_issue",
            joinColumns = {
                    @JoinColumn(name = "issue_period_id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "issue_id")
            }
    )
    private List<Issue> issues;

    private Double avgLeadTime;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Histogram histogram;

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

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Double> leadTimeByPriority;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Chart<String, Long> throughputByPriority;

    private Integer issuesCount;

    private String jql;

    private Double wipAvg;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<DynamicChart> dynamicCharts;

    public String getDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("[%s - %s]", startDate.format(formatter), endDate.format(formatter));
    }
}
