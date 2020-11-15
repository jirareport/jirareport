package br.com.jiratorio.jira.provider

import br.com.jiratorio.domain.issue.FindIssueResult
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.jira.searcher.PagedIssueSearcher
import br.com.jiratorio.jira.service.JiraQueryLanguageService
import br.com.jiratorio.provider.IssueProvider
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class JiraIssueProvider(
    private val jiraQueryLanguageService: JiraQueryLanguageService,
    private val pagedIssueSearcher: PagedIssueSearcher,
) : IssueProvider {

    override fun findClosedIssues(board: BoardEntity, holidays: List<LocalDate>, startDate: LocalDate, endDate: LocalDate): FindIssueResult {
        val query = jiraQueryLanguageService.buildFinalizedIssueQuery(board, startDate, endDate)
        val issues = pagedIssueSearcher.search(query, board, holidays)

        return FindIssueResult(
            query = query,
            issues = issues
        )
    }

    override fun findOpenIssues(board: BoardEntity, holidays: List<LocalDate>): FindIssueResult {
        val query = jiraQueryLanguageService.buildOpenedIssueQuery(board)
        val issues = pagedIssueSearcher.search(query, board, holidays, parseUnfinishedIssue = true)

        return FindIssueResult(
            query = query,
            issues = issues
        )
    }

}
