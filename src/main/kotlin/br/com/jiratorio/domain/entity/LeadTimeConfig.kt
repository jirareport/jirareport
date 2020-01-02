package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsComparing
import br.com.jiratorio.extension.toStringBuilder
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class LeadTimeConfig(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(optional = false)
    var board: Board,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var startColumn: String,

    @Column(nullable = false)
    var endColumn: String

) : BaseEntity() {
    companion object {
        private val serialVersionUID = -1181175426509346889L
    }

    override fun equals(other: Any?) =
        equalsComparing(other, LeadTimeConfig::name, LeadTimeConfig::startColumn, LeadTimeConfig::endColumn)

    override fun hashCode() =
        Objects.hash(name, startColumn, endColumn)

    override fun toString() =
        toStringBuilder(
            LeadTimeConfig::id,
            LeadTimeConfig::name,
            LeadTimeConfig::startColumn,
            LeadTimeConfig::endColumn
        )
}
