package br.com.jiratorio.config

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.jira.JiraError
import br.com.jiratorio.exception.JiraException
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.stereotype.Component

@Component
class FeignErrorDecoder(
    private val objectMapper: ObjectMapper,
    private val messageResolver: MessageResolver
) : ErrorDecoder {

    override fun decode(methodKey: String, response: Response) = JiraException(
        try {
            objectMapper.readValue(response.body().asInputStream(), JiraError::class.java)
        } catch (e: JsonParseException) {
            JiraError(messageResolver.resolve("error.session-timeout"), response.status().toLong())
        }
    )

}
