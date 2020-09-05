package br.com.jiratorio.repository

import br.com.jiratorio.domain.AverageLeadTime

interface LeadTimeNativeRepository {

    fun findAverageLeadTime(issues: List<Long>): List<AverageLeadTime>

}
