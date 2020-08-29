package br.com.jiratorio

import com.github.javafaker.Faker
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Random

@Configuration
class FakerConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun faker() = Faker(Random(System.currentTimeMillis().also {
        log.info("MSG=generated seed, seed={}", it)
    }))

}
