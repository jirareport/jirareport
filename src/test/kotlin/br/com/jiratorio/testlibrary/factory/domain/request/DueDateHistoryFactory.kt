package br.com.jiratorio.testlibrary.factory.domain.request

import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.testlibrary.extension.toLocalDate
import br.com.jiratorio.testlibrary.extension.toLocalDateTime
import br.com.jiratorio.testlibrary.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class DueDateHistoryFactory(
    private val faker: Faker
) : KBacon<DueDateHistory>() {

    override fun builder(): DueDateHistory {
        return DueDateHistory(
            created = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime(),
            dueDate = faker.date().past(15, TimeUnit.DAYS).toLocalDate()
        )
    }

}
