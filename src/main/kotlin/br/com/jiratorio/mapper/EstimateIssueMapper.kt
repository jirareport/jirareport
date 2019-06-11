package br.com.jiratorio.mapper

import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.response.EstimateIssueResponse
import java.time.format.DateTimeFormatter

private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

private val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun EstimateIssue.toEstimateIssueResponse(jiraUrl: String): EstimateIssueResponse =
    EstimateIssueResponse(
        key = key,
        summary = summary,
        startDate = startDate.format(dateTimePattern),
        estimateDateAvg = estimateDateAvg.format(datePattern),
        estimateDatePercentile50 = estimateDatePercentile50.format(datePattern),
        estimateDatePercentile75 = estimateDatePercentile75.format(datePattern),
        estimateDatePercentile90 = estimateDatePercentile90.format(datePattern),
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

fun List<EstimateIssue>.toEstimateIssueResponse(jiraUrl: String): List<EstimateIssueResponse> =
    map { it.toEstimateIssueResponse(jiraUrl) }

