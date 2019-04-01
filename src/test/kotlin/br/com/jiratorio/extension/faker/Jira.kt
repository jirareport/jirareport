package br.com.jiratorio.extension.faker

import com.github.javafaker.Faker

class Jira(
    private val faker: Faker
) {

    fun customField(): String {
        return faker.expression("custom_field_#{number.number_between '1000','9999'}")
    }

    fun issueType(): String {
        return faker.options().option(
            "Story", "Task", "SubTask", "Epic"
        )
    }

    fun system(): String {
        return faker.options().option(
            "JiraReport Api", "JiraReport Web"
        )
    }

    fun epic(): String {
        return faker.options().option(
            "JiraReport V1", "JiraReport V2", "JiraReport V3"
        )
    }

    fun estimate(): String {
        return faker.options().option(
            "P", "M", "G"
        )
    }

    fun project(): String {
        return faker.options().option(
            "jirareport-web", "jirareport-api"
        )
    }

    fun priority(): String {
        return faker.options().option(
            "Expedite", "Major", "Medium", "Normal", "Low"
        )
    }

}
