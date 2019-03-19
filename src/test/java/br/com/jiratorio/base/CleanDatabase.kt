package br.com.jiratorio.base

import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.Statement
import javax.sql.DataSource

@Component
class CleanDatabase(
    private val dataSource: DataSource
) {

    fun clean() {
        dataSource.connection.use { connection ->
            connection.createStatement().use { stmt ->
                truncateTables(connection, stmt)
            }
        }
    }

    private fun truncateTables(connection: Connection, stmt: Statement) {
        connection.metaData.getTables(null, null, null, arrayOf("TABLE")).use {
            while (it.next()) {
                val tableName = it.getString("TABLE_NAME")
                if ("flyway_schema_history" != tableName) {
                    stmt.executeUpdate("TRUNCATE TABLE $tableName RESTART IDENTITY CASCADE")
                }
            }
        }
    }
}
