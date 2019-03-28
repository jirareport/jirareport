package br.com.jiratorio.config.internationalization

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.LocaleResolver
import javax.servlet.http.HttpServletRequest

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class MessageResolver(
    private val messageSource: MessageSource,
    private val localeResolver: LocaleResolver,
    private val request: HttpServletRequest
) {

    fun resolve(key: String, vararg args: Any?): String {
        val locale = localeResolver.resolveLocale(request)
        return messageSource.getMessage(key, args, locale)
    }

}
