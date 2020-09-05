package br.com.jiratorio.usecase.issue.create

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.repository.ColumnChangelogRepository
import br.com.jiratorio.repository.ImpedimentHistoryRepository
import br.com.jiratorio.repository.IssueRepository
import org.springframework.transaction.annotation.Transactional

@UseCase
class PersistIssue(
    private val issueRepository: IssueRepository,
    private val impedimentHistoryRepository: ImpedimentHistoryRepository,
    private val columnChangelogRepository: ColumnChangelogRepository
) {

    @Transactional
    fun execute(issue: IssueEntity): IssueEntity =
        issueRepository.save(issue)
            .also(this::saveImpedimentHistory)
            .also(this::saveColumnChangelog)

    private fun saveImpedimentHistory(issue: IssueEntity) =
        issue.impedimentHistory
            .forEach { impedimentHistory ->
                impedimentHistory.issueId = issue.id
                impedimentHistoryRepository.save(impedimentHistory)
            }

    private fun saveColumnChangelog(issue: IssueEntity) =
        issue.columnChangelog
            .forEach { columnChangelog ->
                columnChangelog.issueId = issue.id
                columnChangelogRepository.save(columnChangelog)
            }

}
