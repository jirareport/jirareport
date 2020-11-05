package br.com.jiratorio.testlibrary.extension.faker

import com.github.javafaker.Faker

class Jira(
    private val faker: Faker
) {

    fun customField(): String =
        faker.expression("custom_field_#{number.number_between '1000','9999'}")

    fun issueType(): String =
        takeOne("Story", "Task", "SubTask", "Epic")

    fun system(): String =
        takeOne("JiraReport Api", "JiraReport Web")

    fun epic(): String =
        takeOne("JiraReport V1", "JiraReport V2", "JiraReport V3")

    fun estimate(): String =
        takeOne("P", "M", "G")

    fun project(): String =
        takeOne("jirareport-web", "jirareport-api")

    fun priority(): String =
        takeOne("Expedite", "Major", "Medium", "Normal", "Low")

    fun leadTime(): Double =
        faker.number().randomDouble(2, 1, 10)

    fun issueLeadTime(): Long =
        faker.number().numberBetween(1, 10).toLong()

    fun throughput(): Int =
        faker.number().numberBetween(1, 15)

    fun column(): String =
        takeOne("ANALYSIS", "DEV WIP", "DEV DONE", "TEST WIP", "TEST DONE", "REVIEW", "DELIVERY LINE", "ACCOMPANIMENT")

    private fun <T> takeOne(vararg items: T): T =
        faker.options().option(*items)

}
