package br.com.jiratorio.factory.domain

import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ColumnChangelogFactory(
    private val faker: Faker
) : KBacon<ColumnChangelog>() {

    override fun builder(): ColumnChangelog {
        return ColumnChangelog(
            from = faker.lorem().word(),
            to = faker.lorem().word(),
            startDate = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime(),
            endDate = faker.date().past(10, TimeUnit.DAYS).toLocalDateTime(),
            leadTime = faker.number().randomNumber()
        )
    }

}
