package br.com.jiratorio.repository.jdbctemplate

import br.com.jiratorio.domain.AverageLeadTime
import br.com.jiratorio.repository.NativeLeadTimeRepository
import br.com.jiratorio.repository.jdbctemplate.rowmapper.AverageLeadTimeRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository

@Repository
class NativeLeadTimeRepositoryImpl(
    jdbcTemplate: JdbcTemplate,
) : NativeLeadTimeRepository {

    private val jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun findAverageLeadTime(issues: List<Long>): List<AverageLeadTime> {
        val query =
            """
                SELECT ltc.name, 
                       avg(lt.lead_time) as value
                FROM lead_time lt
                         INNER JOIN lead_time_config ltc ON lt.lead_time_config_id = ltc.id
                WHERE lt.issue_id in (:issues)
                GROUP BY ltc.name
            """

        val params = MapSqlParameterSource()
        params["issues"] = issues

        return jdbcTemplate.query(query, params, AverageLeadTimeRowMapper)
    }

}
