package br.com.jiratorio.service

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.DueDateHistory

interface DueDateService {

    fun extractDueDateHistory(dueDateCF: String, changelogItems: List<JiraChangelogItem>): List<DueDateHistory>

}
