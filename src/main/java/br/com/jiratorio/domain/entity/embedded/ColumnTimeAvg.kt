package br.com.jiratorio.domain.entity.embedded

import java.io.Serializable

data class ColumnTimeAvg(
    var columnName: String? = null,
    var avgTime: Double? = null
) : Serializable {
    companion object {
        private const val serialVersionUID = -3791819163293059573L
    }
}
