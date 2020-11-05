package br.com.jiratorio.testlibrary.factory.domain

import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.testlibrary.extension.toLocalDateTime
import br.com.jiratorio.testlibrary.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ColumnChangelogFactory(
    private val faker: Faker
) : KBacon<ColumnChangelogEntity>() {

    override fun builder(): ColumnChangelogEntity {
        return ColumnChangelogEntity(
            from = faker.lorem().word(),
            to = faker.lorem().word(),
            startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime(),
            endDate = faker.date().past(10, TimeUnit.DAYS).toLocalDateTime(),
            leadTime = faker.number().randomNumber()
        )
    }

}
