package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.ImpedimentHistory
import br.com.jiratorio.extension.equalsComparing
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "impediment_history")
data class ImpedimentHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "issue_id", nullable = false)
    var issueId: Long = 0,

    @Column(nullable = false)
    override var startDate: LocalDateTime,

    @Column(nullable = false)
    override var endDate: LocalDateTime,

    @Column(nullable = false)
    override var leadTime: Long

) : BaseEntity(), ImpedimentHistory {

    override fun equals(other: Any?): Boolean =
        equalsComparing(
            other,
            ImpedimentHistoryEntity::id,
            ImpedimentHistoryEntity::issueId,
            ImpedimentHistoryEntity::leadTime
        )

    override fun hashCode(): Int =
        Objects.hash(id, issueId, leadTime)

}
