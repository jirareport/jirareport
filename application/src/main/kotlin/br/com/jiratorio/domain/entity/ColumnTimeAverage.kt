package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ColumnTimeAverage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "issue_period_id", nullable = false)
    var issuePeriodId: Long = 0,

    @Column(nullable = false)
    var columnName: String,

    @Column(nullable = false)
    val averageTime: Double

) : BaseEntity() {

    override fun equals(other: Any?): Boolean =
        equalsComparing(
            other,
            ColumnTimeAverage::issuePeriodId,
            ColumnTimeAverage::columnName
        )

    override fun hashCode(): Int =
        Objects.hash(issuePeriodId, columnName)

}
