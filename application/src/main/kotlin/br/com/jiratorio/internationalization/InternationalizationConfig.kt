package br.com.jiratorio.internationalization

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
internal class InternationalizationConfig {

    @Bean
    fun messageSource(): MessageSource {
        return ResourceBundleMessageSource().apply {
            setBasenames(
                "i18n/messages",
                "org/hibernate/validator/ValidationMessages"
            )
            setDefaultEncoding("UTF-8")
        }
    }

    @Bean
    fun localeValidatorFactoryBean(messageSource: MessageSource): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean().also {
            it.setValidationMessageSource(messageSource)
        }
    }

}
