package br.com.jiratorio.parser

import br.com.jiratorio.domain.jira.changelog.JiraChangelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class ChangelogParser(
    private val objectMapper: ObjectMapper
) {

    fun extractChangelogItems(issue: JsonNode): List<JiraChangelogItem> {
        val changelog = objectMapper.treeToValue(issue.path("changelog"), JiraChangelog::class.java)
        changelog.histories.forEach { cl -> cl.items.forEach { i -> i.created = cl.created } }
        return changelog.histories.map { it.items }.flatten()
    }

}
