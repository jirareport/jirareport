package br.com.jiratorio.repository.jdbctemplate

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.extension.jdbctemplate.queryForSet
import br.com.jiratorio.repository.NativeBoardRepository
import br.com.jiratorio.repository.jdbctemplate.rowmapper.BoardPreferencesRowMapper
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository

@Repository
class NativeBoardRepositoryImpl(
    jdbcTemplate: JdbcTemplate,
) : NativeBoardRepository {

    private val jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun findAllOwners(): Set<String> {
        val query =
            """
            SELECT DISTINCT owner 
            FROM board
            """

        return jdbcTemplate.queryForSet(query)
    }

    override fun findIssuePeriodPreferencesByBoard(boardId: Long): BoardPreferences? {
        val query = """
            SELECT id,
                   issue_period_name_format,
                   estimatecf is not null                                     AS has_estimate_feature_enabled,
                   exists(select 1 from lead_time_config where board_id = id) AS has_multiple_lead_time_feature_enabled
            FROM board
            where id = :id
        """

        val params = MapSqlParameterSource()
        params["id"] = boardId

        return try {
            jdbcTemplate.queryForObject(query, params, BoardPreferencesRowMapper)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }


}
