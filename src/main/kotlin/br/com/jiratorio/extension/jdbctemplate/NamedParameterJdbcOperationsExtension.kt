package br.com.jiratorio.extension.jdbctemplate

import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource

inline fun <reified T> NamedParameterJdbcOperations.queryForSet(sql: String, sqlParameterSource: SqlParameterSource = MapSqlParameterSource()): Set<T> =
    query(sql, sqlParameterSource, SetRowMapperResultSetExtractor(SingleColumnRowMapper(T::class.java))) ?: emptySet()
