package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.LeadTimeComparisonByPeriod
import br.com.jiratorio.extension.jdbctemplate.getLocalDate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

object LeadTimeComparisonByPeriodRowMapper : RowMapper<LeadTimeComparisonByPeriod> {

    override fun mapRow(rs: ResultSet, rowNum: Int): LeadTimeComparisonByPeriod =
        LeadTimeComparisonByPeriod(
            periodStart = rs.getLocalDate("period_start"),
            periodEnd = rs.getLocalDate("period_end"),
            leadTimeName = rs.getString("lead_time_name"),
            leadTime = rs.getDouble("lead_time")
        )

}
