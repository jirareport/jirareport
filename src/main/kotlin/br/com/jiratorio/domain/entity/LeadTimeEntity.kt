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
import javax.persistence.Table

@Entity
@Table(name = "lead_time")
data class LeadTimeEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(optional = false)
    var leadTimeConfig: LeadTimeConfigEntity,

    @ManyToOne(optional = false)
    var issue: IssueEntity,

    @Column(nullable = false)
    var leadTime: Long,

    @Column(nullable = false)
    var startDate: LocalDateTime,

    @Column(nullable = false)
    var endDate: LocalDateTime

) : BaseEntity() {
    companion object {
        private const val serialVersionUID = 2918615471478687270L
    }

    override fun equals(other: Any?) =
        equalsComparing(other, LeadTimeEntity::leadTimeConfig, LeadTimeEntity::leadTime, LeadTimeEntity::startDate, LeadTimeEntity::endDate)

    override fun hashCode() =
        Objects.hash(leadTimeConfig, leadTime, startDate, endDate)

}
