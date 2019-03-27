package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.duedate.DueDateType
import br.com.jiratorio.domain.dynamicfield.DynamicFieldConfig
import br.com.jiratorio.domain.impediment.ImpedimentType
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.extension.faker.jira
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.Arrays.asList

@Component
class UpdateBoardRequestFactory(
    private val faker: Faker
) : JBacon<UpdateBoardRequest>() {

    override fun getDefault() =
        UpdateBoardRequest(name = faker.lorem().word()).apply {
            startColumn = faker.lorem().word()
            endColumn = faker.lorem().word()
            fluxColumn = faker.lorem().words()
            ignoreIssueType = faker.lorem().words()
            epicCF = faker.jira().customField()
            estimateCF = faker.jira().customField()
            systemCF = faker.jira().customField()
            projectCF = faker.jira().customField()
            ignoreWeekend = faker.random().nextBoolean()
            impedimentType = faker.options().option(ImpedimentType::class.java)
            impedimentColumns = faker.lorem().words()
            dynamicFields = asList(
                DynamicFieldConfig("dnf_1", faker.jira().customField()),
                DynamicFieldConfig("dnf_2", faker.jira().customField()),
                DynamicFieldConfig("dnf_3", faker.jira().customField())
            )
            touchingColumns = faker.lorem().words()
            waitingColumns = faker.lorem().words()
            dueDateCF = faker.jira().customField()
            dueDateType = faker.options().option(DueDateType::class.java)
        }

    override fun getEmpty() =
        UpdateBoardRequest(name = faker.lorem().word())

    override fun persist(updateBoardRequest: UpdateBoardRequest) =
        throw UnsupportedOperationException()

}
