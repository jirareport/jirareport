package br.com.jiratorio.extension.faker

import com.github.javafaker.Faker

fun Faker.jira() = Jira(this)
