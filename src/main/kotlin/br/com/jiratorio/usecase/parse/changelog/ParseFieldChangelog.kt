package br.com.jiratorio.usecase.parse.changelog

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.FieldChangelog
import br.com.jiratorio.domain.jira.JiraChangelog

@UseCase
class ParseFieldChangelog {

    fun execute(jiraChangelog: List<JiraChangelog>): Set<FieldChangelog> =
        jiraChangelog
            .mapNotNull {
                if (it.field == null)
                    null
                else
                    FieldChangelog(from = it.from, to = it.to, field = it.field, created = it.created)
            }
            .toSet()

}
