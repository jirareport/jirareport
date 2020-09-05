package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import br.com.jiratorio.extension.toStringBuilder
import java.time.LocalDate
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

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
    companion object {
        private val serialVersionUID = 18640912961216513L
    }

    override fun toString() =
        toStringBuilder(HolidayEntity::id, HolidayEntity::date, HolidayEntity::description)

    override fun equals(other: Any?) =
        equalsComparing(other, HolidayEntity::date, HolidayEntity::board)

    override fun hashCode() =
        Objects.hash(date, board)

}
