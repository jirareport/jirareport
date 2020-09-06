package br.com.jiratorio.repository

import br.com.jiratorio.domain.AverageLeadTime

interface NativeLeadTimeRepository {

    fun findAverageLeadTime(issues: List<Long>): List<AverageLeadTime>
    
}
