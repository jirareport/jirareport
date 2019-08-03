package br.com.jiratorio.base

import org.testcontainers.containers.PostgreSQLContainer

object JiraReportPostgresContainer : PostgreSQLContainer<JiraReportPostgresContainer>("$IMAGE:10.9") {

    init {
        withDatabaseName("jirareport_test")
        start()
    }

}
