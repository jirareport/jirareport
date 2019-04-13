package br.com.jiratorio.domain.dynamicfield

import br.com.jiratorio.domain.entity.embedded.Chart
import java.io.Serializable

data class DynamicChart(
    var name: String,
    var leadTime: Chart<String, Double>,
    var throughput: Chart<String, Int>
) : Serializable {
    companion object {
        private const val serialVersionUID = 5429746843879854333L
    }

}
