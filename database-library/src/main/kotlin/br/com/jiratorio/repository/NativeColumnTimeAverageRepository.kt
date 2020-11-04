package br.com.jiratorio.repository

import br.com.jiratorio.domain.response.ColumnTimeAverageResponse

internal interface NativeColumnTimeAverageRepository {

    fun findColumnTimeAverage(issues: List<Long>): List<ColumnTimeAverageResponse>

}
