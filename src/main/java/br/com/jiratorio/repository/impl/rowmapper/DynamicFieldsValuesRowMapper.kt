package br.com.jiratorio.repository.impl.rowmapper

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.ResultSetMetaData

class DynamicFieldsValuesRowMapper(
    private val objectMapper: ObjectMapper
) : RowMapper<List<DynamicFieldsValues>> {

    override fun mapRow(rs: ResultSet, rowNum: Int): List<DynamicFieldsValues>? {
        val metaData: ResultSetMetaData = rs.metaData
        val dynamicFieldsValues = ArrayList<DynamicFieldsValues>()
        for (i in 1..metaData.columnCount) {
            val columnLabel: String = metaData.getColumnLabel(i)
            val values: List<String> = parseList(rs, columnLabel)
            dynamicFieldsValues.add(DynamicFieldsValues(columnLabel, values))
        }
        return dynamicFieldsValues
    }

    private fun parseList(rs: ResultSet, columnLabel: String): List<String> {
        return objectMapper.readValue(rs.getString(columnLabel), object : TypeReference<List<String>>() {
        })
    }
}
