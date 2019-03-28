package br.com.jiratorio.config.internationalization

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class InternationalizationConfig {

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
        return LocalValidatorFactoryBean().apply {
            setValidationMessageSource(messageSource)
        }
    }

}
