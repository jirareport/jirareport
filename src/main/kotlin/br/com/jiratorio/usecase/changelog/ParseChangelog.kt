package br.com.jiratorio.usecase.changelog

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.extension.time.daysDiff
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

@UseCase
class ParseChangelog {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(
        changelogItems: List<JiraChangelogItem>,
        issueCreationDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): List<Changelog> {
        log.info(
            "Method=execute, changelogItems={}, holidays={}, ignoreWeekend={}",
            changelogItems, holidays, ignoreWeekend
        )

        val changelog = changelogItems
            .filter { it.field == "status" && it.created != null }
            .map { Changelog(from = it.fromString, to = it.toString, created = it.created!!) }
            .sortedBy { it.created }
            .toMutableList()

        changelog.add(
            0,
            Changelog(
                from = null,
                to = changelog.firstOrNull()?.from,
                created = issueCreationDate
            )
        )

        changelog.forEachIndexed { i, current ->
            if (i + 1 != changelog.size) {
                val next = changelog[i + 1]
                current.leadTime = current.created.daysDiff(next.created, holidays, ignoreWeekend)
                current.endDate = next.created
            }
        }

        return changelog
    }

}
