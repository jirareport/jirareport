package br.com.jiratorio.configuration

import org.slf4j.LoggerFactory
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest
import java.util.UUID

@Component
class CustomErrorAttributes : DefaultErrorAttributes() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getErrorAttributes(webRequest: WebRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val errorAttributes: MutableMap<String, Any> = super.getErrorAttributes(webRequest, options)

        errorAttributes["traceId"] = UUID.randomUUID()
            .toString()
            .also { log.info("traceId={}", it) }

        return errorAttributes
    }

}
