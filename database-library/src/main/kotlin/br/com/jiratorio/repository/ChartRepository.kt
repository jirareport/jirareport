package br.com.jiratorio.repository

import br.com.jiratorio.domain.LeadTimeComparisonByPeriod
import br.com.jiratorio.domain.PerformanceComparisonByIssueType
import br.com.jiratorio.domain.ThroughputByPeriodAndEstimate

interface ChartRepository {

    fun findLeadTimeComparisonByPeriod(issuePeriods: List<Long>): List<LeadTimeComparisonByPeriod>

    fun findThroughputByPeriodAndEstimate(boardId: Long, issuePeriods: List<Long>): List<ThroughputByPeriodAndEstimate>
    
    fun findPerformanceComparisonByIssueType(boardId: Long, issuePeriods: List<Long>): List<PerformanceComparisonByIssueType>

}
