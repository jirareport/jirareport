package br.com.jiratorio.config

import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest
import java.util.UUID

@Component
class CustomErrorAttributes : DefaultErrorAttributes() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getErrorAttributes(webRequest: WebRequest?, includeStackTrace: Boolean): MutableMap<String, Any> {
        val errorAttributes = super.getErrorAttributes(webRequest, false)

        errorAttributes["traceId"] = UUID.randomUUID().toString().also {
            log.info("traceId={}", it)
        }

        return errorAttributes
    }

}
