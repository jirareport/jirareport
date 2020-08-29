package br.com.jiratorio.config

import br.com.jiratorio.extension.headersWithoutAuth
import feign.Logger
import feign.Request
import feign.Response
import feign.Util
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class FeignLogger : Logger() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun logRequest(configKey: String, logLevel: Level, request: Request) {
        log.info("Method=logRequest, configKey={}, method={}, url={}", configKey, request.httpMethod(), request.url())

        if (logLevel.ordinal >= Level.HEADERS.ordinal) {
            log.info("Method=logRequest, configKey={}, headers={}", configKey, request.headersWithoutAuth())

            if (request.body() != null && logLevel.ordinal >= Level.FULL.ordinal) {
                log.info("Method=logRequest, configKey={}, body={}", configKey, String(request.body(), Charsets.UTF_8))
            }
        }
    }

    override fun logRetry(configKey: String, logLevel: Level?) {
        log.info("Method=logRetry, I=retry, configKey={}", configKey)
    }

    override fun logAndRebufferResponse(
        configKey: String,
        logLevel: Level,
        response: Response,
        elapsedTime: Long
    ): Response {
        log.info(
            "Method=logAndRebufferResponse, configKey={}, status={}, elapsedTime={}",
            configKey, response.status(), elapsedTime
        )

        if (logLevel.ordinal >= Level.HEADERS.ordinal) {
            log.info(
                "Method=logAndRebufferResponse, configKey={}, headers={}",
                configKey,
                response.headersWithoutAuth()
            )

            if (response.body() != null && !(response.status() == 204 || response.status() == 205)) {
                val bodyData = Util.toByteArray(response.body().asInputStream())
                if (logLevel.ordinal >= Level.FULL.ordinal && bodyData.isNotEmpty()) {
                    log.info(
                        "Method=logAndRebufferResponse, configKey={}, body={}",
                        configKey, Util.decodeOrDefault(bodyData, Util.UTF_8, "Binary data")
                    )
                }
                return response.toBuilder().body(bodyData).build()
            }
        }
        return response
    }

    override fun logIOException(
        configKey: String,
        logLevel: Level,
        ioe: IOException,
        elapsedTime: Long
    ): IOException {
        log.info(
            "Method=logIOException, configKey={}, elapsedTime={}, E={}, Err={}",
            configKey, elapsedTime, ioe.javaClass.simpleName, ioe.message
        )

        if (logLevel.ordinal >= Level.FULL.ordinal) {
            log.error("Method=logIOException, configKey={}", configKey, ioe)
        }

        return ioe
    }

    override fun log(configKey: String, format: String, vararg args: Any) {
        log.info(String.format(methodTag(configKey) + format, *args))
    }

}
