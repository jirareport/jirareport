package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import br.com.jiratorio.domain.response.issue.IssueResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class IssueMapper(
    @Value("\${jira.url}")
    private val jiraUrl: String,
    private val leadTimeMapper: LeadTimeMapper,
    private val changelogMapper: ChangelogMapper,
    private val dueDateHistoryMapper: DueDateHistoryMapper
) {

    fun issueToIssueResponse(issues: Collection<Issue>): List<IssueResponse> {
        return issues.map { issueToIssueResponse(it) }
    }

    fun issueToIssueResponse(issue: Issue): IssueResponse {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return IssueResponse(
            id = issue.id,
            key = issue.key,
            creator = issue.creator,
            summary = issue.summary,
            issueType = issue.issueType,
            estimate = issue.estimate,
            project = issue.project,
            epic = issue.epic,
            system = issue.system,
            priority = issue.priority,
            leadTime = issue.leadTime,
            startDate = issue.startDate.format(formatter),
            endDate = issue.endDate.format(formatter),
            created = issue.created.format(formatter),
            deviationOfEstimate = issue.deviationOfEstimate,
            changeEstimateCount = issue.dueDateHistory?.size,
            impedimentTime = issue.impedimentTime,
            dynamicFields = issue.dynamicFields,
            detailsUrl = "$jiraUrl/browse/${issue.key}"
        )
    }

    fun issueToIssueDetailResponse(issue: Issue): IssueDetailResponse {
        return IssueDetailResponse(
            id = issue.id,
            key = issue.key,
            changelog = changelogMapper.changelogToChangelogResponse(issue.changelog),
            dueDateHistory = dueDateHistoryMapper.dueDateHistoryToDueDateHistoryResponse(issue.dueDateHistory),
            waitTime = issue.waitTime / 60.0,
            touchTime = issue.touchTime / 60.0,
            pctEfficiency = issue.pctEfficiency,
            leadTimes = leadTimeMapper.leadTimeToLeadTimeResponse(issue.leadTimes)
        )
    }

}
