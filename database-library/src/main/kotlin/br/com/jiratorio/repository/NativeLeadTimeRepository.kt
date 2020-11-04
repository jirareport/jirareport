package br.com.jiratorio.repository

import br.com.jiratorio.domain.AverageLeadTime

internal interface NativeLeadTimeRepository {

    fun findAverageLeadTime(issues: List<Long>): List<AverageLeadTime>

}
