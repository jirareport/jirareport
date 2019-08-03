package br.com.jiratorio.base.executionlistener

import br.com.jiratorio.extension.log
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import java.sql.Connection
import java.sql.Statement
import javax.sql.DataSource

class CleanDatabaseTestExecutionListener : AbstractTestExecutionListener() {

    override fun beforeTestMethod(testContext: TestContext) {
        try {
            testContext.applicationContext
                .getBean(DataSource::class.java)
                .clean()
        } catch (e: NoSuchBeanDefinitionException) {
            log.info("Method=beforeTestMethod, Info=Not a integration test")
        }
    }

    private fun DataSource.clean() =
        connection.use { connection ->
            connection.createStatement().use { stmt ->
                truncateTables(connection, stmt)
            }
        }

    private fun truncateTables(connection: Connection, stmt: Statement) =
        connection.metaData.getTables(null, null, null, arrayOf("TABLE")).use {
            while (it.next()) {
                val tableName = it.getString("TABLE_NAME")
                if ("flyway_schema_history" != tableName) {
                    stmt.executeUpdate("TRUNCATE TABLE $tableName RESTART IDENTITY CASCADE")
                }
            }
        }
}
