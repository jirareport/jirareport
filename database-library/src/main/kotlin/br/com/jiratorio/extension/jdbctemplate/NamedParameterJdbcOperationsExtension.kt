package br.com.jiratorio.extension.jdbctemplate

import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource

inline fun <reified T> NamedParameterJdbcOperations.queryForSet(sql: String, sqlParameterSource: SqlParameterSource): Set<T> =
    query(sql, sqlParameterSource, SetRowMapperResultSetExtractor(SingleColumnRowMapper<T>(T::class.java))) ?: emptySet()
