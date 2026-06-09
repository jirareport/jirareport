package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.extension.equalsComparing
import br.com.jiratorio.extension.toStringBuilder
import java.util.Objects
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_config")
data class UserConfigEntity(

    @Column(unique = true, nullable = false)
    val username: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var state: String? = null,

    var city: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var leadTimeChartType: ChartType = ChartType.BAR,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var throughputChartType: ChartType = ChartType.DOUGHNUT

) : BaseEntity() {

    override fun toString() =
        toStringBuilder(
            UserConfigEntity::id,
            UserConfigEntity::username,
            UserConfigEntity::state,
            UserConfigEntity::city,
            UserConfigEntity::leadTimeChartType,
            UserConfigEntity::throughputChartType
        )

    override fun equals(other: Any?) =
        equalsComparing(other, UserConfigEntity::username)

    override fun hashCode() =
        Objects.hash(username)

    companion object {
        private val serialVersionUID = -9168105728096346993L
    }

}
