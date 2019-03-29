package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.toStringBuilder
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

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

    var estimated: String? = null,

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

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var changelog: List<Changelog>,

    @ManyToMany(mappedBy = "issues", cascade = [CascadeType.DETACH])
    var issuePeriods: List<IssuePeriod>? = null,

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var leadTimes: Set<LeadTime>? = null,

    @ManyToOne(optional = false)
    var board: Board,

    var deviationOfEstimate: Long? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var dueDateHistory: List<DueDateHistory>? = null,

    var impedimentTime: Long? = null,

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
        private val serialVersionUID = -1084659211505084402L
    }

    override fun toString() =
        toStringBuilder(Issue::id)

}
