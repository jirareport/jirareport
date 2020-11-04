package br.com.jiratorio.extension.jdbctemplate

import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

internal class SetRowMapperResultSetExtractor<T>(
    private val rowMapper: RowMapper<T>
) : ResultSetExtractor<Set<T>> {

    override fun extractData(rs: ResultSet): Set<T> {
        val results: MutableSet<T> = HashSet()

        var rowNum = 0
        while (rs.next()) {
            rowMapper.mapRow(rs, rowNum++)
                ?.let(results::add)
        }

        return results
    }

}
