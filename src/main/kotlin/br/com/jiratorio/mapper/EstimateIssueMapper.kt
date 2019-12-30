package br.com.jiratorio.mapper

import br.com.jiratorio.domain.estimate.EstimatedIssue
import br.com.jiratorio.domain.response.EstimateIssueResponse
import br.com.jiratorio.extension.time.displayFormat

fun EstimatedIssue.toEstimateIssueResponse(jiraUrl: String): EstimateIssueResponse =
    EstimateIssueResponse(
        key = key,
        summary = summary,
        startDate = startDate.displayFormat(),
        estimateDateAvg = estimateDateAvg.displayFormat(),
        estimateDatePercentile50 = estimateDatePercentile50.displayFormat(),
        estimateDatePercentile75 = estimateDatePercentile75.displayFormat(),
        estimateDatePercentile90 = estimateDatePercentile90.displayFormat(),
        leadTime = leadTime,
        issueType = issueType,
        creator = creator,
        estimate = estimate,
        system = system,
        project = project,
        epic = epic,
        priority = priority,
        changelog = changelog.toChangelogResponse(),
        impedimentTime = impedimentTime,
        impedimentHistory = impedimentHistory.toImpedimentHistoryResponse(),
        detailsUrl = "$jiraUrl/browse/$key"
    )

fun List<EstimatedIssue>.toEstimateIssueResponse(jiraUrl: String): List<EstimateIssueResponse> =
    map { it.toEstimateIssueResponse(jiraUrl) }
