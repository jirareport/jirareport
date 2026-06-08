package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import java.time.LocalDateTime
import java.util.Objects
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

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
