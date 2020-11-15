package br.com.jiratorio.domain

data class BoardPreferences(
    val boardId: Long,
    val issuePeriodNameFormat: IssuePeriodNameFormat,
    val hasEstimateFeatureEnabled: Boolean,
    val hasMultipleLeadTimeFeatureEnabled: Boolean,
)
