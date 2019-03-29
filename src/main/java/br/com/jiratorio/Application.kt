package br.com.jiratorio

import brave.sampler.Sampler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@EnableCaching
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
@EnableAspectJAutoProxy
class Application {

    @Bean
    fun executor(): Executor {
        return ConcurrentTaskExecutor(Executors.newFixedThreadPool(10))
    }

    @Bean
    fun sampler(): Sampler = Sampler.ALWAYS_SAMPLE

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
