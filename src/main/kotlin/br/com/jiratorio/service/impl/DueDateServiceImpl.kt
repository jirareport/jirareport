package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.service.DueDateService
import br.com.jiratorio.usecase.duedate.ExtractDueDateHistory
import org.springframework.stereotype.Service

@Service
class DueDateServiceImpl(
    private val extractDueDateHistory: ExtractDueDateHistory
) : DueDateService {

    override fun extractDueDateHistory(
        dueDateCF: String,
        changelogItems: List<JiraChangelogItem>
    ): List<DueDateHistory> =
        extractDueDateHistory.execute(dueDateCF, changelogItems)

}
