package br.com.jiratorio.testlibrary.extension.faker

import net.datafaker.Faker

fun Faker.jira() = Jira(this)
