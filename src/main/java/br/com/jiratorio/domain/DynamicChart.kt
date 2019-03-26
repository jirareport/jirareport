package br.com.jiratorio.domain

import br.com.jiratorio.domain.entity.embedded.Chart
import java.io.Serializable

data class DynamicChart(
    var name: String? = null,
    var leadTime: Chart<String, Double>? = null,
    var throughput: Chart<String, Long>? = null
) : Serializable {
    companion object {
        private const val serialVersionUID = 5429746843879854333L
    }

}
