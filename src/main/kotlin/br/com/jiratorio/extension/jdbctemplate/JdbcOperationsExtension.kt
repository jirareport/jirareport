package br.com.jiratorio.extension.jdbctemplate

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.SingleColumnRowMapper

inline fun <reified T : Any> JdbcOperations.queryForSet(sql: String, args: Array<out Any>): Set<T> =
    query(sql, SetRowMapperResultSetExtractor(SingleColumnRowMapper(T::class.java)), *args).filterNotNull().toSet()
