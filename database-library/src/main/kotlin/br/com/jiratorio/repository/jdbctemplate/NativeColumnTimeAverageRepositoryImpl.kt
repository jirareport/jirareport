package br.com.jiratorio.repository.jdbctemplate

import br.com.jiratorio.domain.response.ColumnTimeAverageResponse
import br.com.jiratorio.repository.NativeColumnTimeAverageRepository
import br.com.jiratorio.repository.jdbctemplate.rowmapper.ColumnTimeAverageResponseRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository

@Repository
class NativeColumnTimeAverageRepositoryImpl(
    jdbcTemplate: JdbcTemplate,
) : NativeColumnTimeAverageRepository {

    private val jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun findColumnTimeAverage(issues: List<Long>): List<ColumnTimeAverageResponse> {
        val query = """
            SELECT 
                column_to as column_name, 
                avg(lead_time) average_time
            FROM column_changelog
            WHERE issue_id IN (:issues)
            GROUP BY column_to;
        """

        val params = MapSqlParameterSource()
        params["issues"] = issues

        return jdbcTemplate.query(query, params, ColumnTimeAverageResponseRowMapper)
    }

}
