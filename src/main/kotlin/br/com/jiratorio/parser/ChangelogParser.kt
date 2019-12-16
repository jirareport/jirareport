package br.com.jiratorio.parser

import br.com.jiratorio.domain.jira.changelog.JiraChangelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.springframework.stereotype.Service

@Service
class ChangelogParser(
    private val objectMapper: ObjectMapper
) {

    fun extractChangelogItems(issue: JsonNode): List<JiraChangelogItem> {
        val changelog: JiraChangelog = objectMapper.treeToValue(issue.path("changelog"))

        changelog.histories.forEach { jiraChangelogHistory ->
            jiraChangelogHistory.items.forEach { jiraChangelogItem ->
                jiraChangelogItem.created = jiraChangelogHistory.created
            }
        }

        return changelog.histories.map { it.items }.flatten()
    }

}
