package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.Chart

fun <K, V> Map<K, V>.toChart(): Chart<K, V> =
    Chart(this)
