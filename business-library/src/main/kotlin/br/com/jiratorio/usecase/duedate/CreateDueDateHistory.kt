package br.com.jiratorio.usecase.duedate

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.FieldChangelog
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.extension.fromJiraToLocalDateTime
import br.com.jiratorio.extension.toLocalDate
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

@UseCase
class CreateDueDateHistory {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(
        dueDateCF: String,
        fieldChangelog: Set<FieldChangelog>
    ): List<DueDateHistory> {
        log.info("Action=createDueDateHistory, dueDateCF={}, fieldChangelog={}", dueDateCF, fieldChangelog)

        return fieldChangelog
            .filter { dueDateCF == it.field }
            .mapNotNull { parseDueDate(it.to, it.created) }
            .sortedBy { it.created }
    }

    private fun parseDueDate(to: String?, created: LocalDateTime): DueDateHistory? =
        to?.let { dueDate ->
            if (dueDate.isEmpty()) {
                return null
            }

            DueDateHistory(
                created = created,
                dueDate = if (dueDate.length > 19)
                    dueDate.fromJiraToLocalDateTime().toLocalDate()
                else
                    dueDate.toLocalDate("[yyyy-MM-dd][dd/MM/yyyy]")
            )
        }

}
