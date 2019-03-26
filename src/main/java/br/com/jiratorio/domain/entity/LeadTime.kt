package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsBuilder
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class LeadTime(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    var leadTimeConfig: LeadTimeConfig? = null,

    @ManyToOne
    var issue: Issue? = null,

    var leadTime: Long? = null,

    var startDate: LocalDateTime? = null,

    var endDate: LocalDateTime? = null
) : BaseEntity() {
    companion object {
        private val serialVersionUID = 2918615471478687270L
    }

    override fun equals(other: Any?) =
        equalsBuilder(other, LeadTime::leadTimeConfig, LeadTime::leadTime, LeadTime::startDate, LeadTime::endDate)

    override fun hashCode() =
        Objects.hash(leadTimeConfig, leadTime, startDate, endDate)

}
