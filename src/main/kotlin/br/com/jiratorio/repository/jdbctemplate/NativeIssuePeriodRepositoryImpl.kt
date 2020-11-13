package br.com.jiratorio.repository.jdbctemplate

import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.issue.MinimalIssuePeriod
import br.com.jiratorio.repository.NativeIssuePeriodRepository
import br.com.jiratorio.repository.jdbctemplate.rowmapper.MinimalIssuePeriodRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository

@Repository
class NativeIssuePeriodRepositoryImpl(
    jdbcTemplate: JdbcTemplate,
) : NativeIssuePeriodRepository {

    private val jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun findAll(filter: FindAllIssuePeriodsFilter): List<MinimalIssuePeriod> {
        val query =
            """
                SELECT ip.id,
                       ip.start_date,
                       ip.end_date,
                       ip.jql,
                       ip.lead_time,
                       ip.throughput,
                       ip.wip_avg,
                       ip.avg_pct_efficiency
                FROM issue_period ip
                WHERE ip.start_date >= :startDate
                AND ip.end_date <= :endDate
                AND ip.board_id = :boardId
                ORDER BY ip.start_date
            """

        val params = MapSqlParameterSource()
        params["boardId"] = filter.boardId
        params["startDate"] = filter.startDate
        params["endDate"] = filter.endDate

        return jdbcTemplate.query(query, params, MinimalIssuePeriodRowMapper)
    }

}
