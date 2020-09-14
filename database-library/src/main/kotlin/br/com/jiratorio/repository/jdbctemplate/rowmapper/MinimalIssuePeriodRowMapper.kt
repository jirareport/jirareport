package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.issue.MinimalIssuePeriod
import br.com.jiratorio.extension.jdbctemplate.getLocalDate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

object MinimalIssuePeriodRowMapper : RowMapper<MinimalIssuePeriod> {

    override fun mapRow(rs: ResultSet, rowNum: Int): MinimalIssuePeriod =
        MinimalIssuePeriod(
            id = rs.getLong("id"),
            startDate = rs.getLocalDate("start_date"),
            endDate = rs.getLocalDate("end_date"),
            jql = rs.getString("jql"),
            leadTime = rs.getDouble("lead_time"),
            throughput = rs.getInt("throughput"),
            wipAvg = rs.getDouble("wip_avg"),
            avgPctEfficiency = rs.getDouble("avg_pct_efficiency")
        )

}
