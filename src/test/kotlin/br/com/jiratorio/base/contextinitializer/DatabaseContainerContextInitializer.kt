package br.com.jiratorio.base.contextinitializer

import br.com.jiratorio.base.JiraReportPostgresContainer
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class DatabaseContainerContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=${JiraReportPostgresContainer.jdbcUrl}",
            "spring.datasource.username=${JiraReportPostgresContainer.username}",
            "spring.datasource.password=${JiraReportPostgresContainer.password}"
        ).applyTo(applicationContext.environment)
    }

}
