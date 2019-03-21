package br.com.jiratorio.config

import br.com.jiratorio.extension.logger
import com.github.javafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Random

@Configuration
class FakerConfig {

    private val log = logger()

    @Bean
    fun faker() = Faker(Random(System.currentTimeMillis().also {
        log.info("I=generated seed, seed={}", it)
    }))

}
