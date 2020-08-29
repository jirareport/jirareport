package br.com.jiratorio.junit.testtype

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
annotation class IntegrationTest
