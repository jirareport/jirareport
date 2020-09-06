package br.com.jiratorio.repository.jdbctemplate

import br.com.jiratorio.domain.LeadTimeComparisonByPeriod
import br.com.jiratorio.domain.PerformanceComparisonByIssueType
import br.com.jiratorio.domain.ThroughputByPeriodAndEstimate
import br.com.jiratorio.repository.ChartRepository
import br.com.jiratorio.repository.jdbctemplate.rowmapper.LeadTimeComparisonByPeriodRowMapper
import br.com.jiratorio.repository.jdbctemplate.rowmapper.PerformanceComparisonByIssueTypeRowMapper
import br.com.jiratorio.repository.jdbctemplate.rowmapper.ThroughputByPeriodAndEstimateRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository

@Repository
class ChartRepositoryImpl(
    jdbcTemplate: JdbcTemplate,
) : ChartRepository {

    private val jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun findLeadTimeComparisonByPeriod(issuePeriods: List<Long>): List<LeadTimeComparisonByPeriod> {
        val query =
            """
            SELECT ip.start_date                  AS period_start,
                   ip.end_date                    AS period_end,
                   ltc.name                       AS lead_time_name,
                   COALESCE(AVG(lt.lead_time), 0) AS lead_time
            FROM issue_period ip
                     LEFT JOIN issue i ON ip.id = i.issue_period_id
                     LEFT JOIN lead_time lt ON i.id = lt.issue_id
                     LEFT JOIN lead_time_config ltc ON ltc.board_id = ip.board_id
            WHERE ip.id IN (:issuePeriods)
            GROUP BY ip.id, ip.start_date, ip.end_date, ltc.name
            ORDER BY ip.start_date, ip.end_date, ltc.name
            """

        val params = MapSqlParameterSource()
        params["issuePeriods"] = issuePeriods

        return jdbcTemplate.query(query, params, LeadTimeComparisonByPeriodRowMapper)
    }

    override fun findThroughputByPeriodAndEstimate(boardId: Long, issuePeriods: List<Long>): List<ThroughputByPeriodAndEstimate> {
        val query =
            """
            WITH issue_estimate AS (
                SELECT DISTINCT estimate,
                                board_id
                FROM issue
                WHERE board_id = :boardId
            )
            SELECT ip.start_date AS period_start,
                   ip.end_date   AS period_end,
                   ie.estimate   AS estimate,
                   COUNT(i)      AS throughput
            FROM issue_period ip
                     INNER JOIN issue_estimate ie ON ie.board_id = ip.board_id
                     LEFT JOIN issue i ON ip.id = i.issue_period_id AND i.estimate IS NOT DISTINCT FROM ie.estimate
            WHERE ip.id IN (:issuePeriods)
            GROUP BY ip.start_date, ip.end_date, ie.estimate
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId
        params["issuePeriods"] = issuePeriods

        return jdbcTemplate.query(query, params, ThroughputByPeriodAndEstimateRowMapper)
    }

    override fun findPerformanceComparisonByIssueType(boardId: Long, issuePeriods: List<Long>): List<PerformanceComparisonByIssueType> {
        val query =
            """
            WITH issue_type AS (
                SELECT DISTINCT issue_type, 
                                board_id
                FROM issue
                WHERE board_id = :boardId
            )
            SELECT it.issue_type                 AS issue_type,
                   ip.start_date                 AS period_start,
                   ip.end_date                   AS period_end,
                   COALESCE(AVG(i.lead_time), 0) AS lead_time,
                   COUNT(i)                      AS throughput
            FROM issue_period ip
                     INNER JOIN issue_type it ON it.board_id = ip.board_id
                     LEFT JOIN issue i ON ip.id = i.issue_period_id AND i.issue_type IS NOT DISTINCT FROM it.issue_type
            WHERE ip.id IN (:issuePeriods)
            GROUP BY ip.start_date, ip.end_date, it.issue_type
            ORDER BY ip.start_date, ip.end_date, it.issue_type
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId
        params["issuePeriods"] = issuePeriods

        return jdbcTemplate.query(query, params, PerformanceComparisonByIssueTypeRowMapper)
    }
}
