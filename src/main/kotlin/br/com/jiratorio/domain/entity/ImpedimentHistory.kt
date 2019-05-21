package br.com.jiratorio.domain.entity

import java.time.LocalDateTime
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

) : Comparable<ImpedimentHistory>, BaseEntity() {

    override fun compareTo(other: ImpedimentHistory): Int {
        return this.startDate.compareTo(other.startDate)
    }

}
