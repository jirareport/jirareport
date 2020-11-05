package br.com.jiratorio.testlibrary.factory.domain

import br.com.jiratorio.domain.changelog.FieldChangelog
import br.com.jiratorio.testlibrary.extension.format
import br.com.jiratorio.testlibrary.extension.toLocalDateTime
import br.com.jiratorio.testlibrary.factory.KBacon
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
