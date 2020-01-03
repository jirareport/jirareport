package br.com.jiratorio.usecase.parse.changelog

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.domain.jira.JiraChangelog
import br.com.jiratorio.extension.time.daysDiff
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

@UseCase
class ParseColumnChangelog {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(
        jiraChangelog: List<JiraChangelog>,
        issueCreationDate: LocalDateTime,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Set<ColumnChangelog> {
        log.info(
            "Action=parseColumnChangelog, jiraChangelog={}, holidays={}, ignoreWeekend={}",
            jiraChangelog, holidays, ignoreWeekend
        )

        val changelog = jiraChangelog
            .filter { it.field == "status" }
            .mapNotNull {
                if (it.to == null)
                    null
                else
                    ColumnChangelog(
                        from = it.from,
                        to = it.to,
                        startDate = it.created
                    )
            }
            .sortedBy { it.startDate }
            .toMutableList()

        if (changelog.isNotEmpty()) {
            changelog.first().let { (_, from, _, _, _, _) ->
                if (from != null) {
                    changelog.add(
                        0,
                        ColumnChangelog(
                            from = null,
                            to = from,
                            startDate = issueCreationDate
                        )
                    )
                }
            }
        }

        changelog.forEachIndexed { i, current ->
            if (i + 1 != changelog.size) {
                val next = changelog[i + 1]
                current.leadTime = current.startDate.daysDiff(next.startDate, holidays, ignoreWeekend)
                current.endDate = next.startDate
            }
        }

        return changelog.toSet()
    }

}
