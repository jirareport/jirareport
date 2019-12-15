package br.com.jiratorio.domain.entity.embedded

import java.io.Serializable

data class ColumnTimeAvg(

    val columnName: String,

    val avgTime: Double

) : Serializable {
    companion object {
        private const val serialVersionUID = -3791819163293059573L
    }
}
