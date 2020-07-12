package br.com.jiratorio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JiratorioApplication

fun main(args: Array<String>) {
	runApplication<JiratorioApplication>(*args)
}
