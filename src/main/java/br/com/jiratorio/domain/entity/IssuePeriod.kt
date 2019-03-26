package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import br.com.jiratorio.domain.entity.embedded.Histogram
import br.com.jiratorio.extension.toStringBuilder
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OrderBy

@Entity
data class IssuePeriod(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var startDate: LocalDate,

    @Column(nullable = false)
    var endDate: LocalDate,

    var boardId: Long? = null,

    @OrderBy("key asc")
    @ManyToMany(cascade = [CascadeType.DETACH], fetch = FetchType.LAZY)
    @JoinTable(
        name = "issue_period_issue",
        joinColumns = [JoinColumn(name = "issue_period_id")],
        inverseJoinColumns = [JoinColumn(name = "issue_id")]
    )
    var issues: MutableList<Issue>? = null,

    var avgLeadTime: Double? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var histogram: Histogram? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeBySize: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var estimated: Chart<String, Long>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeBySystem: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var tasksBySystem: Chart<String, Long>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeByType: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var tasksByType: Chart<String, Long>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeByProject: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var tasksByProject: Chart<String, Long>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeByPriority: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var throughputByPriority: Chart<String, Long>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var columnTimeAvgs: MutableList<ColumnTimeAvg>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeCompareChart: Chart<String, Double>? = null,

    @Column(nullable = false)
    var issuesCount: Int,

    var jql: String? = null,

    var wipAvg: Double? = null,

    var avgPctEfficiency: Double? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var dynamicCharts: MutableList<DynamicChart>? = null
) : BaseEntity() {
    companion object {
        private val serialVersionUID = 7188140641247774389L
    }

    val dates: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return String.format("[%s - %s]", this.startDate!!.format(formatter), this.endDate!!.format(formatter))
        }

    override fun toString() =
        toStringBuilder(
            IssuePeriod::id,
            IssuePeriod::startDate,
            IssuePeriod::endDate,
            IssuePeriod::boardId,
            IssuePeriod::avgLeadTime,
            IssuePeriod::histogram,
            IssuePeriod::leadTimeBySize,
            IssuePeriod::estimated,
            IssuePeriod::leadTimeBySystem,
            IssuePeriod::tasksBySystem,
            IssuePeriod::leadTimeByType,
            IssuePeriod::tasksByType,
            IssuePeriod::leadTimeByProject,
            IssuePeriod::tasksByProject,
            IssuePeriod::leadTimeByPriority,
            IssuePeriod::throughputByPriority,
            IssuePeriod::columnTimeAvgs,
            IssuePeriod::leadTimeCompareChart,
            IssuePeriod::issuesCount,
            IssuePeriod::jql,
            IssuePeriod::wipAvg,
            IssuePeriod::avgPctEfficiency,
            IssuePeriod::dynamicCharts
        )

}
