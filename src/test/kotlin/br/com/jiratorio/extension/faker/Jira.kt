package br.com.jiratorio.extension.faker

import com.github.javafaker.Faker

class Jira(
    private val faker: Faker
) {

    fun customField(): String =
        faker.expression("custom_field_#{number.number_between '1000','9999'}")

}
