package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.equalsComparing
import br.com.jiratorio.extension.toStringBuilder
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OrderBy

@Entity
data class Issue(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var key: String,

    var issueType: String? = null,

    var creator: String? = null,

    var system: String? = null,

    var epic: String? = null,

    @Column(nullable = false)
    var summary: String,

    var estimate: String? = null,

    var project: String? = null,

    @Column(nullable = false)
    var startDate: LocalDateTime,

    @Column(nullable = false)
    var endDate: LocalDateTime,

    @Column(nullable = false)
    val leadTime: Long,

    @Column(nullable = false)
    var created: LocalDateTime,

    var priority: String? = null,

    @OneToMany
    @OrderBy("startDate")
    @JoinColumn(name = "issue_id")
    var columnChangelog: Set<ColumnChangelog>,

    @Column(name = "issue_period_id", nullable = false)
    var issuePeriodId: Long = 0,

    @OneToMany(mappedBy = "issue", cascade = [CascadeType.REMOVE])
    var leadTimes: MutableSet<LeadTime>? = null,

    @ManyToOne(optional = false)
    var board: Board,

    var deviationOfEstimate: Long? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var dueDateHistory: List<DueDateHistory>? = null,

    @Column(nullable = false)
    var impedimentTime: Long = 0,

    @OneToMany
    @OrderBy("startDate")
    @JoinColumn(name = "issue_id", updatable = false)
    var impedimentHistory: MutableSet<ImpedimentHistory> = mutableSetOf(),

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var dynamicFields: Map<String, String?>? = null,

    @Column(nullable = false)
    var waitTime: Long = 0L,

    @Column(nullable = false)
    var touchTime: Long = 0L,

    @Column(nullable = false)
    var pctEfficiency: Double = 0.0

) : BaseEntity() {
    companion object {
        private const val serialVersionUID = -1084659211505084402L
    }

    override fun toString() =
        toStringBuilder(
            Issue::id,
            Issue::key
        )

    override fun equals(other: Any?): Boolean =
        equalsComparing(
            other,
            Issue::id,
            Issue::key
        )

    override fun hashCode(): Int =
        Objects.hash(id, key)

}
