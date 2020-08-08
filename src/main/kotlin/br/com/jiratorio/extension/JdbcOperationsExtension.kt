package br.com.jiratorio.extension

import br.com.jiratorio.repository.jdbctemplate.rowmapper.SetRowMapperResultSetExtractor
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.SingleColumnRowMapper

inline fun <reified T> JdbcOperations.queryForSet(sql: String, args: Array<out Any>): Set<T> =
    query(sql, args, SetRowMapperResultSetExtractor(SingleColumnRowMapper<T>(T::class.java))) ?: emptySet()
