package br.com.jiratorio.domain

import br.com.jiratorio.domain.issueperiodnameformat.IssuePeriodNameFormat

data class BoardPreferences(
    val boardId: Long,
    val issuePeriodNameFormat: IssuePeriodNameFormat,
    val hasEstimateFeatureEnabled: Boolean,
    val hasMultipleLeadTimeFeatureEnabled: Boolean,
) 
