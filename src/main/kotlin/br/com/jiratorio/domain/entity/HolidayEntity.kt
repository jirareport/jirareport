package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import br.com.jiratorio.extension.toStringBuilder
import java.time.LocalDate
import java.util.Objects
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "holiday",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = [
                "date", "board_id"
            ]
        )
    ]
)
data class HolidayEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var date: LocalDate,

    @Column(nullable = false)
    var description: String,

    @ManyToOne(optional = false)
    var board: BoardEntity

) : BaseEntity() {

    override fun toString() =
        toStringBuilder(HolidayEntity::id, HolidayEntity::date, HolidayEntity::description)

    override fun equals(other: Any?) =
        equalsComparing(other, HolidayEntity::date, HolidayEntity::board)

    override fun hashCode() =
        Objects.hash(date, board)

    companion object {
        private val serialVersionUID = 18640912961216513L
    }

}
