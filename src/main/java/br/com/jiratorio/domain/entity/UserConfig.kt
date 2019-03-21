package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.ChartType
import lombok.Data
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Data
@Entity
data class UserConfig(
    val username: String
) : BaseEntity() {
    companion object {
        private val serialVersionUID = -9168105728096346993L
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(unique = true)
    var state: String? = null

    var city: String? = null

    var holidayToken: String? = null

    @Enumerated(EnumType.STRING)
    var leadTimeChartType: ChartType? = null

    @Enumerated(EnumType.STRING)
    var throughputChartType: ChartType? = null

    override fun toString(): String {
        return "UserConfig(username='$username', id=$id, state=$state, city=$city, holidayToken=$holidayToken, " +
                "leadTimeChartType=$leadTimeChartType, throughputChartType=$throughputChartType)"
    }

}
