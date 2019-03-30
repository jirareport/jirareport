package br.com.jiratorio.base.specification

import io.restassured.builder.ResponseSpecBuilder
import io.restassured.specification.ResponseSpecification
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.equalTo

fun notFound(): ResponseSpecification =
    ResponseSpecBuilder()
        .expectStatusCode(HttpStatus.SC_NOT_FOUND)
        .expectBody("error", equalTo("Not Found"))
        .expectBody("message", equalTo("No message available"))
        .expectBody("status", equalTo(404))
        .build()
