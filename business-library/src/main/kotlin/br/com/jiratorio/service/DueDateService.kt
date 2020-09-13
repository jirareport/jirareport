package br.com.jiratorio.service

import br.com.jiratorio.domain.changelog.FieldChangelog
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.toLocalDate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DueDateService {

    private val log = LoggerFactory.getLogger(javaClass)

    fun parseHistory(dueDateCF: String, fieldChangelog: Set<FieldChangelog>): List<DueDateHistory> {
        log.info("dueDateCF={}, fieldChangelog={}", dueDateCF, fieldChangelog)

        return fieldChangelog
            .filter { dueDateCF == it.field }
            .mapNotNull { parseDueDate(it.to, it.created) }
            .sortedBy { it.created }
    }

    private fun parseDueDate(dueDate: String?, created: LocalDateTime): DueDateHistory? =
        if (dueDate.isNullOrEmpty())
            null
        else
            DueDateHistory(
                created = created,
                dueDate = if (dueDate.length > 19)
                    dueDate.fromJiraToLocalDateTime().toLocalDate()
                else
                    dueDate.toLocalDate("[yyyy-MM-dd][dd/MM/yyyy]")
            )

}
