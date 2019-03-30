package br.com.jiratorio.dsl

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSender
import io.restassured.specification.RequestSpecification

class RestAssuredDSL {

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
        RestAssured
            .given()
            .log().all()
            .header("Accept-Language", "en")
            .apply(blockGiven)
            .`when`()
            .run(blockOn)
            .then()
            .log().all()
            .run(blockThen)
    }

}

fun restAssured(block: RestAssuredDSL.() -> Unit) {
    RestAssuredDSL().apply(block).build()
}
