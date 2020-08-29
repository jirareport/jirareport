package br.com.jiratorio.usecase.jql

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import org.slf4j.LoggerFactory

@UseCase
class CreateOpenedIssueJql {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(board: Board): String {
        log.info("Action=createOpenedIssueJql, board={}", board)

        val wipColumns = FluxColumn(board).wipColumns.joinToString(",") { "'$it'" }
        val project = board.externalId

        val ignoredIssueTypes = board.ignoreIssueType.let {
            if (it.isNullOrEmpty()) {
                ""
            } else {
                it.joinToString(",", "AND issueType NOT IN (", ")") { "'$it'" }
            }
        }

        return """
            project = '$project'
            AND status IN ($wipColumns)
            $ignoredIssueTypes
        """
            .trimMargin()
            .replace("\n", "")
            .replace("\\", "\\\\")
    }
}
