package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsBuilder
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
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = [
                "date", "board_id"
            ]
        )
    ]
)
data class Holiday(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var date: LocalDate,

    @Column(nullable = false)
    var description: String,

    @ManyToOne(optional = false)
    var board: Board

) : BaseEntity() {
    companion object {
        private val serialVersionUID = 18640912961216513L
    }

    override fun toString() =
        toStringBuilder(Holiday::id, Holiday::date, Holiday::description)

    override fun equals(other: Any?) =
        equalsBuilder(other, Holiday::date, Holiday::board)

    override fun hashCode() =
        Objects.hash(date, board)

}
