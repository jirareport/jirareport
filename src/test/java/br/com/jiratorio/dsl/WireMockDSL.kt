package br.com.jiratorio.dsl

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo

class WireMockDSL {

    private lateinit var returnBlock: ResponseDefinitionBuilder.() -> Unit

    private lateinit var mappingBock: MappingBuilder.() -> Unit

    private lateinit var url: String

    fun get(url: String, block: MappingBuilder.() -> Unit) {
        this.url = url
        this.mappingBock = block
    }

    fun willReturn(block: ResponseDefinitionBuilder.() -> Unit) {
        this.returnBlock = block
    }

    fun build() {
        stubFor(
            get(urlPathEqualTo(this.url))
                .apply(mappingBock)
                .willReturn(aResponse().apply(returnBlock))
        )
    }

}

fun wireMock(block: WireMockDSL.() -> Unit) {
    WireMockDSL().apply(block).build()
}
