package br.com.jiratorio.factory.domain

import br.com.jiratorio.domain.FieldChangelog
import br.com.jiratorio.extension.format
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class FieldChangelogFactory(
    private val faker: Faker
) : KBacon<FieldChangelog>() {

    override fun builder(): FieldChangelog =
        FieldChangelog(
            field = "duedate",
            to = faker.date().future(3, TimeUnit.DAYS).format("yyyy-MM-dd"),
            created = faker.date().past(3, TimeUnit.DAYS).toLocalDateTime()
        )

}
