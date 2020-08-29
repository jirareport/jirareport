package br.com.jiratorio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
