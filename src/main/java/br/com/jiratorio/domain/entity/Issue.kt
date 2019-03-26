package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.ToString
import org.hibernate.annotations.Type

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
import java.time.LocalDateTime

@Entity
data class Issue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var key: String? = null,

    var issueType: String? = null,

    var creator: String? = null,

    var system: String? = null,

    var epic: String? = null,

    var summary: String? = null,

    var estimated: String? = null,

    var project: String? = null,

    var startDate: LocalDateTime? = null,

    var endDate: LocalDateTime? = null,

    @Column(nullable = false)
    val leadTime: Long? = null,

    var created: LocalDateTime? = null,

    var priority: String? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var changelog: List<Changelog>? = null,

    @ManyToMany(mappedBy = "issues", cascade = [CascadeType.DETACH])
    var issuePeriods: List<IssuePeriod>? = null,

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var leadTimes: Set<LeadTime>? = null,

    @ManyToOne
    var board: Board? = null,

    var deviationOfEstimate: Long? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var dueDateHistory: List<DueDateHistory>? = null,

    var impedimentTime: Long? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var dynamicFields: Map<String, String>? = null,

    var waitTime: Long? = null,

    var touchTime: Long? = null,

    var pctEfficiency: Double? = null
) : BaseEntity() {
    companion object {
        private val serialVersionUID = -1084659211505084402L
    }

}
