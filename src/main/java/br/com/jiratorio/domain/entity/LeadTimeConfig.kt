package br.com.jiratorio.domain.entity

import br.com.jiratorio.extension.equalsBuilder
import br.com.jiratorio.extension.toStringBuilder
import java.util.Objects
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class LeadTimeConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    var board: Board? = null,

    var name: String? = null,

    var startColumn: String? = null,

    var endColumn: String? = null
) : BaseEntity() {

    companion object {
        private val serialVersionUID = -1181175426509346889L
    }

    override fun equals(other: Any?) =
        equalsBuilder(other, LeadTimeConfig::name, LeadTimeConfig::startColumn, LeadTimeConfig::endColumn)

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
