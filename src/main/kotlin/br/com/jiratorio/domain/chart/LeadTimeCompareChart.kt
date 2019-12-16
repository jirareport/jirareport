package br.com.jiratorio.domain.chart

import java.io.Serializable
import java.util.LinkedHashMap

class LeadTimeCompareChart(

    val labels: MutableList<String> = ArrayList(),

    val datasources: MutableMap<String, MutableList<Double>> = LinkedHashMap()

) : Serializable {
    companion object {
        private const val serialVersionUID = -1501002922104599319L
    }

    fun add(key: String, collect: Map<String, Double>) {
        labels.add(key)

        collect.forEach { (k, v) ->
            if (datasources.containsKey(k)) {
                val data = datasources[k]

                if (data != null) {
                    data.add(v)
                    datasources[k] = data
                }
            } else {
                val data = ArrayList<Double>()
                data.add(v)

                datasources[k] = data
            }
        }
    }

}
