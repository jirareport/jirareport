package br.com.jiratorio.repository

import br.com.jiratorio.domain.response.ColumnTimeAverageResponse

interface NativeColumnTimeAverageRepository {

    fun findColumnTimeAverage(issues: List<Long>): List<ColumnTimeAverageResponse>

}
