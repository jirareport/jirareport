package br.com.jiratorio.extension

import com.fasterxml.jackson.databind.JsonNode
import java.util.stream.Stream
import java.util.stream.StreamSupport

fun JsonNode?.extractValueNotNull(): String =
    this.extractValue()!!

fun JsonNode?.extractValue(): String? {
    if (this == null || this.isNull || this.isMissingNode) {
        return null
    }

    if (this.isObject) {
        return when {
            this.hasNonNull("value") -> this.path("value").extractValue()
            this.hasNonNull("displayName") -> this.path("displayName").extractValue()
            else -> this.path("name").extractValue()
        }
    }

    if (this.isArray) {
        return this.joinToString {
            it.extractValue() ?: ""
        }
    }

    return this.asText(null)
}

fun JsonNode.parallelStream(): Stream<JsonNode> =
    StreamSupport.stream(spliterator(), true)
