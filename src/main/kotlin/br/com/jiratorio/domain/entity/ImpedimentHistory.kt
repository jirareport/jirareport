package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ImpedimentHistory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "issue_id", nullable = false)
    var issueId: Long = 0,

    @Column(nullable = false)
    var startDate: LocalDateTime,

    @Column(nullable = false)
    var endDate: LocalDateTime,

    @Column(nullable = false)
    var leadTime: Long

) : BaseEntity(), Comparable<ImpedimentHistory> {

    override fun compareTo(other: ImpedimentHistory): Int =
        startDate.compareTo(other.startDate)

    override fun equals(other: Any?): Boolean =
        equalsComparing(
            other,
            ImpedimentHistory::id,
            ImpedimentHistory::issueId,
            ImpedimentHistory::leadTime
        )

    override fun hashCode(): Int =
        Objects.hash(id, issueId, leadTime)

}
