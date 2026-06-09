package br.com.jiratorio.domain.entity.embedded

import java.io.Serializable

data class Histogram(
    val chart: Chart<Long, Int>,
    val median: Long,
    val percentile75: Long,
    val percentile90: Long
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

}
