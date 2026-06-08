package br.com.jiratorio.testlibrary

import org.testcontainers.containers.PostgreSQLContainer

object JiraReportPostgresContainer : PostgreSQLContainer<JiraReportPostgresContainer>("$IMAGE:16") {

    init {
        withDatabaseName("jirareport_test")
        start()
    }

}
