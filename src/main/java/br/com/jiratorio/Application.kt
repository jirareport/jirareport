package br.com.jiratorio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableCaching
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
@EnableAspectJAutoProxy
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
