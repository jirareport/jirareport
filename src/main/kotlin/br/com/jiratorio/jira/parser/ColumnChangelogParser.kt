package br.com.jiratorio.jira.parser

import br.com.jiratorio.domain.changelog.ColumnChangelog
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.jira.domain.JiraChangelog
import br.com.jiratorio.jira.domain.JiraColumnChangelog
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ColumnChangelogParser {

    fun parse(jiraChangelog: List<JiraChangelog>, issueCreationDate: LocalDateTime, holidays: List<LocalDate>, ignoreWeekend: Boolean?, endsToday: Boolean): Set<ColumnChangelog> {
        val changelog = jiraChangelog
            .filter { it.field == "status" }
            .mapNotNull {
                if (it.to == null)
                    null
                else
                    ChangelogBuilder(
                        from = it.from,
                        to = it.to,
                        startDate = it.created
                    )
            }
            .sortedBy { it.startDate }
            .toMutableList()

        if (changelog.isEmpty()) {
            return emptySet()
        }

        changelog.first()
            .from
            ?.let { from ->
                changelog.add(
                    0,
                    ChangelogBuilder(
                        from = null,
                        to = from,
                        startDate = issueCreationDate
                    )
                )
            }

        val columnChangelog = changelog.zipWithNext { current, next ->
            current.buildWithNext(next, holidays, ignoreWeekend)
        } + changelog.last()
            .let { changelogBuilder ->
                if (endsToday)
                    changelogBuilder.buildEndingToday(holidays, ignoreWeekend)
                else
                    changelogBuilder.build()
            }

        return columnChangelog.toSet()
    }

    private class ChangelogBuilder(
        val from: String?,
        val to: String,
        val startDate: LocalDateTime,
    ) {

        fun build() =
            JiraColumnChangelog(
                from = from,
                to = to,
                startDate = startDate
            )

        fun buildEndingToday(holidays: List<LocalDate>, ignoreWeekend: Boolean?) =
            JiraColumnChangelog(
                from = from,
                to = to,
                startDate = startDate,
                endDate = LocalDateTime.now(),
                leadTime = startDate.daysDiff(LocalDateTime.now(), holidays, ignoreWeekend)
            )

        fun buildWithNext(next: ChangelogBuilder, holidays: List<LocalDate>, ignoreWeekend: Boolean?) =
            JiraColumnChangelog(
                from = from,
                to = to,
                startDate = startDate,
                leadTime = startDate.daysDiff(next.startDate, holidays, ignoreWeekend),
                endDate = next.startDate
            )

    }

}
