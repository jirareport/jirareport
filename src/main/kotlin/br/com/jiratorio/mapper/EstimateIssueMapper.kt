package br.com.jiratorio.mapper

import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.response.EstimateIssueResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class EstimateIssueMapper(
    @Value("\${jira.url}")
    private val jiraUrl: String,

    private val changelogMapper: ChangelogMapper
) {

    private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    private val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun estimateIssueToEstimateIssueResponse(estimateIssue: EstimateIssue): EstimateIssueResponse {
        return EstimateIssueResponse(
            key = estimateIssue.key,
            summary = estimateIssue.summary,
            startDate = estimateIssue.startDate.format(dateTimePattern),
            estimateDateAvg = estimateIssue.estimateDateAvg.format(datePattern),
            estimateDatePercentile50 = estimateIssue.estimateDatePercentile50.format(datePattern),
            estimateDatePercentile75 = estimateIssue.estimateDatePercentile75.format(datePattern),
            estimateDatePercentile90 = estimateIssue.estimateDatePercentile90.format(datePattern),
            leadTime = estimateIssue.leadTime,
            issueType = estimateIssue.issueType,
            creator = estimateIssue.creator,
            estimate = estimateIssue.estimate,
            system = estimateIssue.system,
            project = estimateIssue.project,
            epic = estimateIssue.epic,
            priority = estimateIssue.priority,
            changelog = changelogMapper.changelogToChangelogResponse(estimateIssue.changelog),
            impedimentTime = estimateIssue.impedimentTime,
            detailsUrl = "$jiraUrl/browse/${estimateIssue.key}"
        )
    }

    fun estimateIssueToEstimateIssueResponse(estimateIssue: List<EstimateIssue>): List<EstimateIssueResponse> {
        return estimateIssue.map { estimateIssueToEstimateIssueResponse(it) }
    }

}
