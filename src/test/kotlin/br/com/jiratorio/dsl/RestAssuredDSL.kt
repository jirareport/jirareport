package br.com.jiratorio.dsl

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSender
import io.restassured.specification.RequestSpecification
import kotlin.reflect.KClass

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

    fun build(): Response {
        return RestAssured
            .given()
                .log().all()
                .header("Accept-Language", "en")
                .apply(blockGiven)
            .`when`()
                .run(blockOn)
            .then()
                .log().all()
                .apply(blockThen)
                .extract().response()
    }

}

inline fun restAssured(block: RestAssuredDSL.() -> Unit): Response {
    return RestAssuredDSL().apply(block).build()
}

infix fun <T : Any> Response.extractAs(type: KClass<T>): T {
    return this.`as`<T>(type.java)
}
