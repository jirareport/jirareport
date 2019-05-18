package br.com.jiratorio.service

import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.domain.entity.embedded.Changelog
import java.time.LocalDate

interface ChangelogService {

    fun parseChangelog(
        changelogItems: List<JiraChangelogItem>,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): List<Changelog>

}
