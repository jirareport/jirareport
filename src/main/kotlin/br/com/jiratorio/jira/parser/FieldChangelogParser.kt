package br.com.jiratorio.jira.parser

import br.com.jiratorio.domain.changelog.FieldChangelog
import br.com.jiratorio.jira.domain.JiraChangelog
import org.springframework.stereotype.Component

@Component
class FieldChangelogParser {

    fun parse(jiraChangelog: List<JiraChangelog>): Set<FieldChangelog> =
        jiraChangelog
            .mapNotNull(this::toFieldChangelog)
            .toSet()

    private fun toFieldChangelog(it: JiraChangelog): FieldChangelog? =
        if (it.field == null || it.field == "status")
            null
        else
            FieldChangelog(
                from = it.from,
                to = it.to,
                field = it.field,
                created = it.created
            )

}
