package br.com.jiratorio.mapper

import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.domain.entity.embedded.Chart

fun <K, V> Map<K, V>.toChart(): Chart<K, V> =
    Chart(this)

fun <T> Map<String, Map<String, T>>.toMultiAxisChart(): MultiAxisChart<T> {
    val result = MultiAxisChart<T>()
    forEach { result[it.key] = it.value }

    return result
}
