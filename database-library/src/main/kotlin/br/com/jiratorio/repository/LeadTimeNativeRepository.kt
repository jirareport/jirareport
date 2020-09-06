package br.com.jiratorio.repository

import br.com.jiratorio.domain.AverageLeadTime
import br.com.jiratorio.domain.LeadTimeComparisonByPeriod

interface LeadTimeNativeRepository {

    fun findAverageLeadTime(issues: List<Long>): List<AverageLeadTime>
    
    fun findComparisonByPeriod(issuePeriods: List<Long>): List<LeadTimeComparisonByPeriod>

}
