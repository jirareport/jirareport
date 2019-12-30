package br.com.jiratorio.usecase.parse

import br.com.jiratorio.domain.jira.changelog.JiraChangelog
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ParseJiraChangelog(
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issue: JsonNode): List<JiraChangelogItem> {
        log.info("Action=ParseJiraChangelog, issue={}", issue)

        val changelog: JiraChangelog = objectMapper.treeToValue(issue.path("changelog"))

        changelog.histories.forEach { jiraChangelogHistory ->
            jiraChangelogHistory.items.forEach { jiraChangelogItem ->
                jiraChangelogItem.created = jiraChangelogHistory.created
            }
        }

        return changelog.histories.map { it.items }.flatten()
    }

}
