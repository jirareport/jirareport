package br.com.jiratorio.extension

import br.com.jiratorio.domain.entity.embedded.Chart

fun <K, V> Map<K, V>.toChart(): Chart<K, V> {
    return Chart(this)
}
