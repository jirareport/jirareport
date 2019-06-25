package br.com.jiratorio.service

import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.Changelog
import java.time.LocalDate
import java.time.LocalDateTime

interface ChangelogService {

    fun parseChangelog(
        changelogItems: List<JiraChangelogItem>,
        issueCreationDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): List<Changelog>

}
