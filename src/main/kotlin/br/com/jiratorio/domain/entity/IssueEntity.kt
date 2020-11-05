package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.issue.Issue
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
import javax.persistence.Table

@Entity
@Table(name = "issue")
data class IssueEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0,

    @Column(nullable = false)
    override var key: String,

    override var issueType: String? = null,

    override var creator: String? = null,

    override var system: String? = null,

    override var epic: String? = null,

    @Column(nullable = false)
    override var summary: String,

    override var estimate: String? = null,

    override var project: String? = null,

    @Column(nullable = false)
    override var startDate: LocalDateTime,

    @Column(nullable = false)
    override var endDate: LocalDateTime,

    @Column(nullable = false)
    override val leadTime: Long,

    @Column(nullable = false)
    override var created: LocalDateTime,

    override var priority: String? = null,

    @OneToMany
    @OrderBy("startDate")
    @JoinColumn(name = "issue_id")
    var columnChangelog: Set<ColumnChangelogEntity>,

    @Column(name = "issue_period_id", nullable = false)
    var issuePeriodId: Long = 0,

    @OneToMany(mappedBy = "issue", cascade = [CascadeType.REMOVE])
    var leadTimes: MutableSet<LeadTimeEntity>? = null,

    @ManyToOne(optional = false)
    var board: BoardEntity,

    override var deviationOfEstimate: Long? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var dueDateHistory: List<DueDateHistory>? = null,

    @Column(nullable = false)
    override var impedimentTime: Long = 0,

    @OneToMany
    @OrderBy("startDate")
    @JoinColumn(name = "issue_id", updatable = false)
    var impedimentHistory: MutableSet<ImpedimentHistoryEntity> = mutableSetOf(),

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    override var dynamicFields: Map<String, String?> = emptyMap(),

    @Column(nullable = false)
    var waitTime: Long = 0L,

    @Column(nullable = false)
    var touchTime: Long = 0L,

    @Column(nullable = false)
    var pctEfficiency: Double = 0.0,

) : BaseEntity(), Issue {
    companion object {
        private const val serialVersionUID = -1084659211505084402L
    }

    override val changeEstimateCount: Int
        get() = dueDateHistory?.size ?: 0

    override fun toString() =
        toStringBuilder(
            IssueEntity::id,
            IssueEntity::key
        )

    override fun equals(other: Any?): Boolean =
        equalsComparing(
            other,
            IssueEntity::id,
            IssueEntity::key
        )

    override fun hashCode(): Int =
        Objects.hash(id, key)

}
