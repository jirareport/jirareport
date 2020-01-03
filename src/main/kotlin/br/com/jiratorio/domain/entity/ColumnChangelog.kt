package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import br.com.jiratorio.extension.time.LocalDateProgression
import br.com.jiratorio.extension.time.rangeTo
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ColumnChangelog(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "issue_id", nullable = false)
    var issueId: Long = 0,

    @Column(name = "column_from")
    var from: String? = null,

    @Column(name = "column_to", nullable = false)
    var to: String,

    @Column(nullable = false)
    var startDate: LocalDateTime,

    @Column(nullable = false)
    var endDate: LocalDateTime = startDate,

    @Column(nullable = false)
    var leadTime: Long = 0

) : BaseEntity() {

    val dateRange: LocalDateProgression
        get() = startDate.toLocalDate()..endDate.toLocalDate()

    override fun equals(other: Any?): Boolean =
        equalsComparing(
            other,
            ColumnChangelog::from,
            ColumnChangelog::to,
            ColumnChangelog::startDate,
            ColumnChangelog::endDate,
            ColumnChangelog::leadTime
        )

    override fun hashCode(): Int =
        Objects.hash(from, to, startDate, endDate, leadTime)

}
