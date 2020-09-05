package br.com.jiratorio.mapper

import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import br.com.jiratorio.domain.response.issue.IssueResponse
import br.com.jiratorio.extension.time.displayFormat

fun MinimalIssue.toIssueResponse(jiraUrl: String): IssueResponse =
    IssueResponse(
        id = id,
        key = key,
        creator = creator,
        summary = summary,
        issueType = issueType,
        estimate = estimate,
        project = project,
        epic = epic,
        system = system,
        priority = priority,
        leadTime = leadTime,
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        created = created.displayFormat(),
        deviationOfEstimate = deviationOfEstimate,
        changeEstimateCount = changeEstimateCount,
        impedimentTime = impedimentTime,
        dynamicFields = dynamicFields,
        detailsUrl = "$jiraUrl/browse/$key"
    )

fun Issue.toIssueResponse(jiraUrl: String): IssueResponse =
    IssueResponse(
        id = id,
        key = key,
        creator = creator,
        summary = summary,
        issueType = issueType,
        estimate = estimate,
        project = project,
        epic = epic,
        system = system,
        priority = priority,
        leadTime = leadTime,
        startDate = startDate.displayFormat(),
        endDate = endDate.displayFormat(),
        created = created.displayFormat(),
        deviationOfEstimate = deviationOfEstimate,
        changeEstimateCount = dueDateHistory?.size,
        impedimentTime = impedimentTime,
        dynamicFields = dynamicFields,
        detailsUrl = "$jiraUrl/browse/$key"
    )

fun Collection<MinimalIssue>.toIssueResponse(jiraUrl: String): List<IssueResponse> =
    map { it.toIssueResponse(jiraUrl) }

fun Issue.toIssueDetailResponse(): IssueDetailResponse =
    IssueDetailResponse(
        id = id,
        key = key,
        changelog = columnChangelog.toChangelogResponse(),
        dueDateHistory = dueDateHistory?.toDueDateHistoryResponse(),
        impedimentHistory = impedimentHistory.toImpedimentHistoryResponse(),
        waitTime = waitTime / 60.0,
        touchTime = touchTime / 60.0,
        pctEfficiency = pctEfficiency,
        leadTimes = leadTimes?.toLeadTimeResponse()
    )
