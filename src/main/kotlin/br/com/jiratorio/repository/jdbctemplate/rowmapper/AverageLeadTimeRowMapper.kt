package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.AverageLeadTime
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

object AverageLeadTimeRowMapper : RowMapper<AverageLeadTime> {

    override fun mapRow(rs: ResultSet, rowNum: Int): AverageLeadTime =
        AverageLeadTime(
            name = rs.getString("name"),
            value = rs.getDouble("value")
        )

}
