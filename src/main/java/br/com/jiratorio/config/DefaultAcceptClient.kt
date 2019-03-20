package br.com.jiratorio.config

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

@Component
class DefaultAcceptClient : RequestInterceptor {

    override fun apply(template: RequestTemplate?) {
        template?.header("Accept", "application/json")
    }

}
