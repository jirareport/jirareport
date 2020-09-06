package br.com.jiratorio.repository

import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.LeadTimeComparisonByPeriod
import br.com.jiratorio.domain.PerformanceComparisonByIssueType
import br.com.jiratorio.domain.ThroughputByPeriodAndEstimate

interface ChartRepository {

    fun findLeadTimeComparisonByPeriod(filter: FindAllIssuePeriodsFilter): List<LeadTimeComparisonByPeriod>

    fun findThroughputByPeriodAndEstimate(filter: FindAllIssuePeriodsFilter): List<ThroughputByPeriodAndEstimate>
    
    fun findPerformanceComparisonByIssueType(filter: FindAllIssuePeriodsFilter): List<PerformanceComparisonByIssueType>

}
