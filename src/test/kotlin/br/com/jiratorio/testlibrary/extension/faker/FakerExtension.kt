package br.com.jiratorio.testlibrary.extension.faker

import com.github.javafaker.Faker

fun Faker.jira() = Jira(this)
