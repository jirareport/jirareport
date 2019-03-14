package br.com.jiratorio.factory.domain

import br.com.jiratorio.domain.changelog.JiraChangelogItem
import br.com.jiratorio.extension.format
import br.com.jiratorio.extension.toLocalDateTime
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class JiraChangelogItemFactory(
        private val faker: Faker
) : JBacon<JiraChangelogItem>() {

    override fun getDefault() =
            JiraChangelogItem().apply {
                field = "duedate"
                to = faker.date().future(3, TimeUnit.DAYS).format("yyyy-MM-dd")
                created = faker.date().past(3, TimeUnit.DAYS).toLocalDateTime()
            }

    override fun getEmpty() =
            JiraChangelogItem()

    override fun persist(jiraChangelogItem: JiraChangelogItem) =
            throw UnsupportedOperationException()

}
