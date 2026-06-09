package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.chart.DynamicChart
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.Histogram
import br.com.jiratorio.domain.entity.embedded.IssueProgression
import br.com.jiratorio.extension.toStringBuilder
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import java.time.LocalDate
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table

@Entity
@Table(name = "issue_period")
data class IssuePeriodEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var startDate: LocalDate,

    @Column(nullable = false)
    var endDate: LocalDate,

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    var board: BoardEntity,

    @Column(nullable = false)
    var name: String,

    @OneToMany
    @OrderBy("key")
    @JoinColumn(name = "issue_period_id", updatable = false)
    var issues: MutableSet<IssueEntity> = mutableSetOf(),

    @Column(nullable = false)
    var leadTime: Double = 0.0,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var histogram: Histogram? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var leadTimeByEstimate: Chart<String, Double>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var throughputByEstimate: Chart<String, Int>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var leadTimeBySystem: Chart<String, Double>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var throughputBySystem: Chart<String, Int>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var leadTimeByType: Chart<String, Double>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var throughputByType: Chart<String, Int>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var leadTimeByProject: Chart<String, Double>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var throughputByProject: Chart<String, Int>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var leadTimeByPriority: Chart<String, Double>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var throughputByPriority: Chart<String, Int>? = null,

    @OneToMany
    @OrderBy("id")
    @JoinColumn(name = "issue_period_id", updatable = false)
    var columnTimeAverages: Set<ColumnTimeAverageEntity> = mutableSetOf(),

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var leadTimeCompareChart: Chart<String, Double>? = null,

    @Column(nullable = false)
    var throughput: Int = 0,

    @Column(nullable = false)
    var jql: String = "",

    @Column(nullable = false)
    var wipAvg: Double = 0.0,

    @Column(nullable = false)
    var avgPctEfficiency: Double = 0.0,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var dynamicCharts: MutableList<DynamicChart>? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var issueProgression: IssueProgression? = null

) : BaseEntity() {

    override fun toString() =
        toStringBuilder(
            IssuePeriodEntity::id,
            IssuePeriodEntity::board,
            IssuePeriodEntity::startDate,
            IssuePeriodEntity::endDate
        )

    companion object {
        private const val serialVersionUID = 7188140641247774389L
    }

}
