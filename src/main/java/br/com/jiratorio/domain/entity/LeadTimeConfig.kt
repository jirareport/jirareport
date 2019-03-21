package br.com.jiratorio.domain.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class LeadTimeConfig : BaseEntity() {
    companion object {
        private val serialVersionUID = -1181175426509346889L
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    var board: Board? = null

    var name: String? = null

    var startColumn: String? = null

    var endColumn: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LeadTimeConfig

        if (id != other.id) return false
        if (name != other.name) return false
        if (startColumn != other.startColumn) return false
        if (endColumn != other.endColumn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (startColumn?.hashCode() ?: 0)
        result = 31 * result + (endColumn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "LeadTimeConfig(id=$id, name=$name, startColumn=$startColumn, endColumn=$endColumn)"
    }

}
