package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import java.util.Objects
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "column_time_average")
data class ColumnTimeAverageEntity(

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
            ColumnTimeAverageEntity::issuePeriodId,
            ColumnTimeAverageEntity::columnName
        )

    override fun hashCode(): Int =
        Objects.hash(issuePeriodId, columnName)

}
