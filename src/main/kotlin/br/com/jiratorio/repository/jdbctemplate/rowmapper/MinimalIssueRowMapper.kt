package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.issue.MinimalIssue
import br.com.jiratorio.extension.jdbctemplate.getLocalDateTime
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class MinimalIssueRowMapper(
    private val objectMapper: ObjectMapper,
) : RowMapper<MinimalIssue> {

    override fun mapRow(rs: ResultSet, rowNum: Int): MinimalIssue =
        MinimalIssue(
            id = rs.getLong("id"),
            key = rs.getString("key"),
            leadTime = rs.getLong("lead_time"),
            startDate = rs.getLocalDateTime("start_date"),
            endDate = rs.getLocalDateTime("end_date"),
            creator = rs.getString("creator"),
            summary = rs.getString("summary"),
            issueType = rs.getString("issue_type"),
            estimate = rs.getString("estimate"),
            project = rs.getString("project"),
            epic = rs.getString("epic"),
            system = rs.getString("system"),
            priority = rs.getString("priority"),
            created = rs.getLocalDateTime("created"),
            deviationOfEstimate = rs.getLong("deviation_of_estimate"),
            changeEstimateCount = rs.getInt("change_estimate_count"),
            impedimentTime = rs.getLong("impediment_time"),
            dynamicFields = objectMapper.readValue(rs.getString("dynamic_fields")),
        )

}
