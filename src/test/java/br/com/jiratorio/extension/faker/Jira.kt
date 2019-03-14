package br.com.jiratorio.extension.faker

import com.github.javafaker.Faker

class Jira(val faker: Faker) {

    fun customField() = faker.expression("custom_field_#{number.number_between '1000','9999'}")!!

}
