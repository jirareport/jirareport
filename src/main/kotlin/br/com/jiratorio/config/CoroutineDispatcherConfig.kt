package br.com.jiratorio.config

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class CoroutineDispatcherConfig {

    @Bean
    fun issueParserCoroutineDispatcher(): ExecutorCoroutineDispatcher {
        return Executors.newFixedThreadPool(10).asCoroutineDispatcher()
    }

    @Bean
    fun chartCoroutineDispatcher(): ExecutorCoroutineDispatcher {
        return Executors.newFixedThreadPool(10).asCoroutineDispatcher()
    }

}
