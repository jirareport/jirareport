package br.com.jiratorio.domain.entity.embedded

import java.io.Serializable
import java.util.LinkedHashMap

data class Chart<L, V>(
    val data: MutableMap<L, V> = LinkedHashMap()
) : Serializable {
    companion object {
        private const val serialVersionUID = 7550041573002395950L
    }

    fun add(x: L, y: V) {
        data[x] = y
    }

}
