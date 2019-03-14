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
import java.util.Arrays
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

}
