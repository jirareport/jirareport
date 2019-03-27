package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.extension.equalsBuilder
import br.com.jiratorio.extension.toStringBuilder
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class UserConfig(

    @Column(nullable = false)
    val username: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(unique = true)
    var state: String? = null,

    var city: String? = null,

    var holidayToken: String? = null,

    @Enumerated(EnumType.STRING)
    var leadTimeChartType: ChartType? = null,

    @Enumerated(EnumType.STRING)
    var throughputChartType: ChartType? = null

) : BaseEntity() {
    companion object {
        private val serialVersionUID = -9168105728096346993L
    }

    override fun toString() =
        toStringBuilder(
            UserConfig::id,
            UserConfig::username,
            UserConfig::state,
            UserConfig::city,
            UserConfig::holidayToken,
            UserConfig::leadTimeChartType,
            UserConfig::throughputChartType
        )

    override fun equals(other: Any?) =
        equalsBuilder(other, UserConfig::username)

    override fun hashCode() =
        Objects.hash(username)

}
