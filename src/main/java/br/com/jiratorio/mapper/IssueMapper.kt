package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.response.IssueResponse
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class IssueMapper {

    fun issueToIssueResponse(issues: List<Issue>): List<IssueResponse> {
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
            estimate = issue.estimated,
            project = issue.project,
            epic = issue.epic,
            system = issue.system,
            priority = issue.priority,
            leadTime = issue.leadTime,
            startDate = issue.startDate.format(formatter),
            endDate = issue.endDate.format(formatter),
            created = issue.created.format(formatter),
            deviationOfEstimate = issue.deviationOfEstimate,
            dueDateHistory = issue.dueDateHistory,
            impedimentTime = issue.impedimentTime,
            dynamicFields = issue.dynamicFields,
            waitTime = issue.waitTime,
            touchTime = issue.touchTime,
            pctEfficiency = issue.pctEfficiency
        )
    }

}
