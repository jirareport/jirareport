package br.com.jiratorio.factory.entity

import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.DynamicFieldConfig
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.impediment.ImpedimentType
import br.com.jiratorio.extension.faker.jira
import br.com.jiratorio.repository.BoardRepository
import br.com.leonardoferreira.jbacon.JBacon
import br.com.leonardoferreira.jbacon.annotation.JBaconTemplate
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.Arrays.asList

@Component
class BoardFactory(
    private val faker: Faker,
    val boardRepository: BoardRepository
) : JBacon<Board>() {

    override fun getDefault() =
        Board().apply {
            externalId = faker.number().randomNumber()
            name = faker.lorem().word()
        }

    override fun getEmpty() = Board()

    override fun persist(board: Board) {
        boardRepository.save(board)
    }

    @JBaconTemplate
    protected fun fullBoard() =
        default.apply {
            startColumn = "TODO"
            endColumn = "DONE"
            fluxColumn = asList("TODO", "WIP", "DONE")
            ignoreIssueType = mutableListOf("IT_1")
            epicCF = faker.jira().customField()
            estimateCF = faker.jira().customField()
            systemCF = faker.jira().customField()
            projectCF = faker.jira().customField()
            ignoreWeekend = false
            impedimentType = ImpedimentType.COLUMN
            impedimentColumns = asList("IMP_COLUMN1", "IMP_COLUMN2", "IMP_COLUMN3")
            dynamicFields = asList(
                DynamicFieldConfig("dn_field1", faker.jira().customField()),
                DynamicFieldConfig("dn_field2", faker.jira().customField())
            )
            touchingColumns = faker.lorem().words()
            waitingColumns = faker.lorem().words()
            dueDateCF = faker.jira().customField()
            dueDateType = DueDateType.FIRST_DUE_DATE_AND_END_DATE
        }

    @JBaconTemplate
    protected fun withBasicConfiguration() =
        default.apply {
            startColumn = "TODO"
            endColumn = "DONE"
            fluxColumn = asList("BACKLOG", "TODO", "WIP", "ACCOMPANIMENT", "DONE")
        }

    @JBaconTemplate
    protected fun withCompleteConfiguration() =
        default.apply {
            startColumn = "ANALYSIS"
            endColumn = "DONE"
            fluxColumn = asList(
                "BACKLOG", "ANALYSIS", "DEV WIP", "DEV DONE", "TEST WIP", "TEST DONE", "REVIEW", "ACCOMPANIMENT", "DONE"
            )
            ignoreIssueType = mutableListOf("SubTask")
            epicCF = "customfield_1000"
            estimateCF = "customfield_2000"
            systemCF = "customfield_3000"
            projectCF = "customfield_4000"
            ignoreWeekend = false
            impedimentType = ImpedimentType.FLAG
            impedimentColumns = mutableListOf()
            touchingColumns = asList(
                "ANALYSIS", "DEV WIP", "TEST WIP", "REVIEW", "ACCOMPANIMENT"
            )
            waitingColumns = asList(
                "BACKLOG", "DEV DONE", "TEST DONE"
            )
            dueDateCF = "duedate"
            dueDateType = DueDateType.FIRST_DUE_DATE_AND_END_DATE
        }

    @JBaconTemplate
    protected fun withDynamicFields() =
            withCompleteConfiguration().apply {
                dynamicFields = mutableListOf(
                    DynamicFieldConfig(
                        name = "Team",
                        field = "customfield_5000"
                    ),
                    DynamicFieldConfig(
                        name = "Level Of Dependency",
                        field = "customfield_6000"
                    )
                )
            }

}
