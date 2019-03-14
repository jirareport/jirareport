package br.com.jiratorio.base.specification

import io.restassured.builder.ResponseSpecBuilder
import org.apache.http.HttpStatus
import org.hamcrest.Matchers

fun notFound() =
        ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_NOT_FOUND)
                .expectBody("error", Matchers.`is`("Not Found"))
                .expectBody("message", Matchers.`is`("Resource Not Found"))
                .expectBody("status", Matchers.`is`(404))
                .build()!!

