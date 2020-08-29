package br.com.jiratorio.usecase.parse.changelog

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.FieldChangelog
import br.com.jiratorio.domain.jira.JiraChangelog

@UseCase
class ParseFieldChangelog {

    fun execute(jiraChangelog: List<JiraChangelog>): Set<FieldChangelog> =
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
