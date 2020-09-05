package br.com.jiratorio.extension.jdbctemplate

import java.sql.ResultSet
import java.time.LocalDateTime

fun ResultSet.getLocalDateTime(columnLabel: String): LocalDateTime =
    getObject(columnLabel, LocalDateTime::class.java)
