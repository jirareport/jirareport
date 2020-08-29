package br.com.jiratorio.jira

import br.com.jiratorio.extension.buildList
import com.fasterxml.jackson.annotation.JsonProperty

data class JiraError(

    val message: String?,

    @field:JsonProperty("status-code")
    val statusCode: Long?,

    @field:JsonProperty("errorMessages")
    val messages: List<String>? = null

) {

    val allErrors: List<String>
        get() = buildList {
            message?.let { add(it) }
            messages?.let { addAll(it) }
        }

}
