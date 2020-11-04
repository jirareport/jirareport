package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.response.ColumnTimeAverageResponse
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

internal object ColumnTimeAverageResponseRowMapper : RowMapper<ColumnTimeAverageResponse> {

    override fun mapRow(rs: ResultSet, rowNum: Int): ColumnTimeAverageResponse =
        ColumnTimeAverageResponse(
            columnName = rs.getString("column_name"),
            averageTime = rs.getDouble("average_time")
        )

}
