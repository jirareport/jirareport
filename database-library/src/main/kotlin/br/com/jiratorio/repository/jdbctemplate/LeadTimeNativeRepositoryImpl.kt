package br.com.jiratorio.repository.jdbctemplate

import br.com.jiratorio.domain.AverageLeadTime
import br.com.jiratorio.domain.LeadTimeComparisonByPeriod
import br.com.jiratorio.repository.LeadTimeNativeRepository
import br.com.jiratorio.repository.jdbctemplate.rowmapper.AverageLeadTimeRowMapper
import br.com.jiratorio.repository.jdbctemplate.rowmapper.LeadTimeComparisonByPeriodRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository

@Repository
class LeadTimeNativeRepositoryImpl(
    jdbcTemplate: JdbcTemplate,
) : LeadTimeNativeRepository {

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

    override fun findComparisonByPeriod(issuePeriods: List<Long>): List<LeadTimeComparisonByPeriod> {
        val query =
            """
            select ip.start_date as period_start,
                   ip.end_date as period_end,
                   ltc.name as lead_time_name,
                   coalesce(avg(lt.lead_time), 0) as lead_time
            from issue_period ip
                     left join issue i on ip.id = i.issue_period_id
                     left join lead_time lt on i.id = lt.issue_id
                     join lead_time_config ltc on ltc.board_id = ip.board_id
            where ip.id in (:issuePeriods)
            group by ip.id, ip.start_date, ip.end_date, ltc.name
            order by ip.start_date, ip.end_date;  
            """

        val params = MapSqlParameterSource()
        params["issuePeriods"] = issuePeriods

        return jdbcTemplate.query(query, params, LeadTimeComparisonByPeriodRowMapper)
    }

}
