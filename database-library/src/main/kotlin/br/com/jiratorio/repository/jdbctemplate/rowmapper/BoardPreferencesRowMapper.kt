package br.com.jiratorio.repository.jdbctemplate.rowmapper

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.domain.issueperiodnameformat.IssuePeriodNameFormat
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

object BoardPreferencesRowMapper : RowMapper<BoardPreferences> {

    override fun mapRow(rs: ResultSet, rowNum: Int): BoardPreferences =
        BoardPreferences(
            boardId = rs.getLong("id"),
            issuePeriodNameFormat = IssuePeriodNameFormat.valueOf(rs.getString("issue_period_name_format")),
            hasEstimateFeatureEnabled = rs.getBoolean("has_estimate_feature_enabled"),
            hasMultipleLeadTimeFeatureEnabled = rs.getBoolean("has_multiple_lead_time_feature_enabled")
        )

}
