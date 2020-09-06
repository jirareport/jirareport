package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.domain.issue.MinimalIssuePeriod
import br.com.jiratorio.extension.jdbctemplate.getLocalDate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class MinimalIssuePeriodRowMapper(
    private val boardPreferences: BoardPreferences,
) : RowMapper<MinimalIssuePeriod> {

    override fun mapRow(rs: ResultSet, rowNum: Int): MinimalIssuePeriod =
        MinimalIssuePeriod(
            id = rs.getLong("id"),
            name = boardPreferences.issuePeriodNameFormat.format(rs.getLocalDate("start_date"), rs.getLocalDate("end_date")),
            jql = rs.getString("jql"),
            leadTime = rs.getDouble("lead_time"),
            throughput = rs.getInt("throughput"),
            wipAvg = rs.getDouble("wip_avg"),
            avgPctEfficiency = rs.getDouble("avg_pct_efficiency")
        )

}
