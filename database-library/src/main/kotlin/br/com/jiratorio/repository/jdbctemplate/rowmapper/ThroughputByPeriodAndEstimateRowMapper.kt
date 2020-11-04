package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.ThroughputByPeriodAndEstimate
import br.com.jiratorio.extension.jdbctemplate.getLocalDate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

internal object ThroughputByPeriodAndEstimateRowMapper : RowMapper<ThroughputByPeriodAndEstimate> {

    override fun mapRow(rs: ResultSet, rowNum: Int): ThroughputByPeriodAndEstimate =
        ThroughputByPeriodAndEstimate(
            periodStart = rs.getLocalDate("period_start"),
            periodEnd = rs.getLocalDate("period_end"),
            estimate = rs.getString("estimate"),
            throughput = rs.getInt("throughput")
        )

}
