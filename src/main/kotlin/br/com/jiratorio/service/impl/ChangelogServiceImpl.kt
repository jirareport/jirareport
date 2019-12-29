package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.service.ChangelogService
import br.com.jiratorio.usecase.changelog.ParseChangelog
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ChangelogServiceImpl(
    private val parseChangelog: ParseChangelog
) : ChangelogService {

    override fun parseChangelog(
        changelogItems: List<JiraChangelogItem>,
        issueCreationDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): List<Changelog> =
        parseChangelog.execute(changelogItems, issueCreationDate, holidays, ignoreWeekend)

}
