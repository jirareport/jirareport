package br.com.jiratorio.base.specification

import io.restassured.builder.ResponseSpecBuilder
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.equalTo

fun notFound() =
    ResponseSpecBuilder()
        .expectStatusCode(HttpStatus.SC_NOT_FOUND)
        .expectBody("error", equalTo("Not Found"))
        .expectBody("message", equalTo("Resource Not Found"))
        .expectBody("status", equalTo(404))
        .build()
