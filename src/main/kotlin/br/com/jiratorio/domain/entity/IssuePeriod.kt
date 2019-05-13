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
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OrderBy

@Entity
data class IssuePeriod(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var startDate: LocalDate,

    @Column(nullable = false)
    var endDate: LocalDate,

    @Column(nullable = false)
    var boardId: Long,

    @OrderBy("key asc")
    @JoinColumn(name = "issue_period_id", updatable = false)
    @OneToMany(orphanRemoval = true, cascade = [CascadeType.ALL])
    var issues: MutableSet<Issue>,

    @Column(nullable = false)
    var leadTime: Double,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var histogram: Histogram? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeByEstimate: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var throughputByEstimate: Chart<String, Int>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeBySystem: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var throughputBySystem: Chart<String, Int>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeByType: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var throughputByType: Chart<String, Int>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeByProject: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var throughputByProject: Chart<String, Int>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeByPriority: Chart<String, Double>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var throughputByPriority: Chart<String, Int>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var columnTimeAvg: MutableList<ColumnTimeAvg>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var leadTimeCompareChart: Chart<String, Double>? = null,

    @Column(nullable = false)
    var throughput: Int,

    @Column(nullable = false)
    var jql: String,

    @Column(nullable = false)
    var wipAvg: Double,

    @Column(nullable = false)
    var avgPctEfficiency: Double,

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
            return "[%s - %s]".format(this.startDate.format(formatter), this.endDate.format(formatter))
        }

    override fun toString() =
        toStringBuilder(
            IssuePeriod::id,
            IssuePeriod::startDate,
            IssuePeriod::endDate,
            IssuePeriod::boardId,
            IssuePeriod::leadTime,
            IssuePeriod::histogram,
            IssuePeriod::leadTimeByEstimate,
            IssuePeriod::throughputByEstimate,
            IssuePeriod::leadTimeBySystem,
            IssuePeriod::throughputBySystem,
            IssuePeriod::leadTimeByType,
            IssuePeriod::throughputByType,
            IssuePeriod::leadTimeByProject,
            IssuePeriod::throughputByProject,
            IssuePeriod::leadTimeByPriority,
            IssuePeriod::throughputByPriority,
            IssuePeriod::columnTimeAvg,
            IssuePeriod::leadTimeCompareChart,
            IssuePeriod::throughput,
            IssuePeriod::jql,
            IssuePeriod::wipAvg,
            IssuePeriod::avgPctEfficiency,
            IssuePeriod::dynamicCharts
        )

}
