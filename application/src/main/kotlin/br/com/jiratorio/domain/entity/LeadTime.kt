package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class LeadTime(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(optional = false)
    var leadTimeConfig: LeadTimeConfig,

    @ManyToOne(optional = false)
    var issue: Issue,

    @Column(nullable = false)
    var leadTime: Long,

    @Column(nullable = false)
    var startDate: LocalDateTime,

    @Column(nullable = false)
    var endDate: LocalDateTime

) : BaseEntity() {
    companion object {
        private val serialVersionUID = 2918615471478687270L
    }

    override fun equals(other: Any?) =
        equalsComparing(other, LeadTime::leadTimeConfig, LeadTime::leadTime, LeadTime::startDate, LeadTime::endDate)

    override fun hashCode() =
        Objects.hash(leadTimeConfig, leadTime, startDate, endDate)

}
