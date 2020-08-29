package br.com.jiratorio.restassured.specification

import io.restassured.builder.ResponseSpecBuilder
import io.restassured.specification.ResponseSpecification
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.equalTo

fun notFound(): ResponseSpecification =
    ResponseSpecBuilder()
        .expectStatusCode(HttpStatus.SC_NOT_FOUND)
        .expectBody("error", equalTo("Not Found"))
        .expectBody("message", equalTo("Resource not found"))
        .expectBody("status", equalTo(404))
        .build()
