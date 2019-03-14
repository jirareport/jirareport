package br.com.jiratorio.dsl

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSender
import io.restassured.specification.RequestSpecification

class RestAssuredBuilder {

    private lateinit var blockGiven: RequestSpecification.() -> Unit

    private lateinit var blockOn: RequestSpecification.() -> Response

    private lateinit var blockThen: ValidatableResponse.() -> Unit

    fun given(block: RequestSpecification.() -> Unit) {
        blockGiven = block
    }

    fun on(block: RequestSender.() -> Response) {
        blockOn = block
    }

    fun then(block: ValidatableResponse.() -> Unit) {
        blockThen = block
    }

    fun build() {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .apply(blockGiven)
                .`when`()
                    .run(blockOn)
                .then()
                    .log().all()
                    .apply(blockThen)
        // @formatter:on
    }

}

fun restAssured(block: RestAssuredBuilder.() -> Unit) {
    RestAssuredBuilder().apply(block).build()
}

