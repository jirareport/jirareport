package br.com.jiratorio.internationalization

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.LocaleResolver
import java.util.Locale
import javax.servlet.http.HttpServletRequest

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
internal class MessageResolverImpl(
    localeResolver: LocaleResolver,
    request: HttpServletRequest,
    private val messageSource: MessageSource
) : MessageResolver {

    override val locale: Locale = localeResolver.resolveLocale(request)

    override fun resolve(key: String, vararg args: Any?): String =
        messageSource.getMessage(key, args, locale)

}
