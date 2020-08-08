package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.ResultSetMetaData

class DynamicFieldsValuesRowMapper(
    private val objectMapper: ObjectMapper
) : RowMapper<List<DynamicFieldsValues>> {

    override fun mapRow(rs: ResultSet, rowNum: Int): List<DynamicFieldsValues> {
        val metaData: ResultSetMetaData = rs.metaData
        return (1..metaData.columnCount).map { i ->
            val columnLabel: String = metaData.getColumnLabel(i)
            val values: List<String> = objectMapper.readValue(rs.getString(columnLabel))
            DynamicFieldsValues(columnLabel, values)
        }
    }

}
