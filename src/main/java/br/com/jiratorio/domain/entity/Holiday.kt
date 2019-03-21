package br.com.jiratorio.domain.entity

import lombok.Data
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Data
@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["date", "board_id"])])
class Holiday : BaseEntity() {
    companion object {
        private val serialVersionUID = 18640912961216513L
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var date: LocalDate? = null

    var description: String? = null

    @ManyToOne
    var board: Board? = null

    override fun toString(): String {
        return "Holiday(id=$id, date=$date, description=$description)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Holiday

        if (date != other.date) return false
        if (board != other.board) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date?.hashCode() ?: 0
        result = 31 * result + (board?.hashCode() ?: 0)
        return result
    }

}
