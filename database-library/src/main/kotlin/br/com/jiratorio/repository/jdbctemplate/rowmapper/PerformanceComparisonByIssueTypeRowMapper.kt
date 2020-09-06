package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.PerformanceComparisonByIssueType
import br.com.jiratorio.extension.jdbctemplate.getLocalDate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

object PerformanceComparisonByIssueTypeRowMapper : RowMapper<PerformanceComparisonByIssueType> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PerformanceComparisonByIssueType =
        PerformanceComparisonByIssueType(
            issueType = rs.getString("issue_type"),
            periodStart = rs.getLocalDate("period_start"),
            periodEnd = rs.getLocalDate("period_end"),
            leadTime = rs.getDouble("lead_time"),
            throughput = rs.getInt("throughput")
        )

}
