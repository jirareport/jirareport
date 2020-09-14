package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.ImpedimentType
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.extension.faker.jira
import br.com.jiratorio.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class UpdateBoardRequestFactory(
    private val faker: Faker
) : KBacon<UpdateBoardRequest>() {

    override fun builder(): UpdateBoardRequest {
        return UpdateBoardRequest(
            name = faker.lorem().word(),
            startColumn = faker.lorem().word(),
            endColumn = faker.lorem().word(),
            fluxColumn = faker.lorem().words(),
            ignoreIssueType = faker.lorem().words(),
            epicCF = faker.jira().customField(),
            estimateCF = faker.jira().customField(),
            systemCF = faker.jira().customField(),
            projectCF = faker.jira().customField(),
            ignoreWeekend = faker.random().nextBoolean(),
            impedimentType = faker.options().option(ImpedimentType::class.java),
            impedimentColumns = faker.lorem().words(),
            touchingColumns = faker.lorem().words(),
            waitingColumns = faker.lorem().words(),
            dueDateCF = faker.jira().customField(),
            dueDateType = faker.options().option(DueDateType::class.java)
        )
    }
}
