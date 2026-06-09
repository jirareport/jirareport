package br.com.jiratorio.extension.jdbctemplate

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.SingleColumnRowMapper

inline fun <reified T : Any> JdbcOperations.queryForSet(sql: String, args: Array<out Any>): Set<T> =
    query(sql, args, SetRowMapperResultSetExtractor(SingleColumnRowMapper(T::class.java)))?.filterNotNull()?.toSet() ?: emptySet()
