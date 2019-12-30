package br.com.jiratorio.usecase.duedate

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.jira.changelog.JiraChangelogItem
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@UseCase
class CreateDueDateHistory {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(
        dueDateCF: String,
        changelogItems: List<JiraChangelogItem>
    ): List<DueDateHistory> {
        log.info("Method=extractDueDateHistory, dueDateCF={}, changelogItems={}", dueDateCF, changelogItems)

        return changelogItems
            .filter { dueDateCF == it.field && !StringUtils.isEmpty(it.to) }
            .map { DueDateHistory(it.created, parseDueDate(it.to)) }
            .sortedBy { it.created }
    }

    private fun parseDueDate(dueDateStr: String?): LocalDate? {
        if (dueDateStr == null || dueDateStr.isEmpty()) {
            return null
        }

        if (dueDateStr.length > 19) {
            val localDateTime = dueDateStr.fromJiraToLocalDateTime()
            return localDateTime.toLocalDate()
        }

        val formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd][dd/MM/yyyy]")
        return LocalDate.parse(dueDateStr, formatter)
    }
}
