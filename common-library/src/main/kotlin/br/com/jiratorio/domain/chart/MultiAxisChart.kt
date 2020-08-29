package br.com.jiratorio.domain.chart

import java.util.LinkedHashMap

data class MultiAxisChart<T>(
    val labels: MutableList<String> = ArrayList(),
    val datasources: MutableMap<String, MutableList<T>> = LinkedHashMap()
) {

    operator fun set(key: String, values: Map<String, T>) {
        labels.add(key)
        values.forEach(::addValue)
    }

    private fun addValue(key: String, value: T) {
        datasources.getOrPut(key, ::mutableListOf).add(value)
    }

}
