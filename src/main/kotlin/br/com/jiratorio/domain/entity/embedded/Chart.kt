package br.com.jiratorio.domain.entity.embedded

import java.io.Serializable
import java.util.LinkedHashMap

data class Chart<L, V>(
    val data: Map<L, V> = LinkedHashMap()
) : Serializable {

    operator fun set(x: L, y: V) {
        if (data is MutableMap) {
            data[x] = y
        }
    }

    companion object {
        private const val serialVersionUID = 7550041573002395950L
    }

}
