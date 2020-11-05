package br.com.jiratorio.jira.client.config

import feign.Request
import feign.RequestInterceptor
import feign.RetryableException
import feign.Retryer
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class GlobalClientConfig {

    @Bean
    fun defaultAcceptClient() = RequestInterceptor { template ->
        template.header("Accept", "application/json")
    }

    @Bean
    fun defaultRetryer(): Retryer {
        class CustomRetryer(
            private val maxAttempt: Int
        ) : Retryer {

            private var attempt = 1

            override fun continueOrPropagate(e: RetryableException) {
                if (e.method() != Request.HttpMethod.GET || attempt++ >= maxAttempt) {
                    throw e
                }

                try {
                    Thread.sleep(250)
                } catch (ex: Exception) {
                    throw e
                }
            }

            override fun clone(): Retryer =
                CustomRetryer(maxAttempt)

        }

        return CustomRetryer(5)
    }

}
