package br.com.jiratorio.repository.jdbctemplate

import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.extension.jdbctemplate.queryForSet
import br.com.jiratorio.extension.time.atEndOfDay
import br.com.jiratorio.repository.NativeIssueRepository
import br.com.jiratorio.repository.jdbctemplate.rowmapper.DynamicFieldsValuesRowMapper
import br.com.jiratorio.repository.jdbctemplate.rowmapper.MinimalIssueRowMapper
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class NativeIssueRepositoryImpl(
    private val objectMapper: ObjectMapper,
    jdbcTemplate: JdbcTemplate
) : NativeIssueRepository {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private val jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    @Transactional(readOnly = true)
    override fun findByExample(
        board: Board,
        dynamicFilters: Map<String, Array<String>>,
        searchIssueRequest: SearchIssueRequest
    ): List<MinimalIssue> {
        log.info("Method=findByExample, board={}, dynamicFilters={}, searchIssueRequest={}", board, searchIssueRequest, dynamicFilters)

        val params = MapSqlParameterSource()

        val query = StringBuilder(
            """
            SELECT issue.id,
                   issue.key, 
                   issue.lead_time,
                   issue.start_date, 
                   issue.end_date, 
                   issue.creator, 
                   issue.summary, 
                   issue.issue_type, 
                   issue.estimate, 
                   issue.project, 
                   issue.epic, 
                   issue.system, 
                   issue.priority, 
                   issue.created, 
                   issue.deviation_of_estimate, 
                   coalesce(jsonb_array_length(issue.due_date_history), 0) as change_estimate_count,
                   issue.impediment_time,
                   issue.dynamic_fields 
            FROM issue
                     LEFT JOIN lead_time ON issue.id = lead_time.issue_id
                     LEFT JOIN lead_time_config ON lead_time.lead_time_config_id = lead_time_config.id
            WHERE issue.board_id = :boardId
              AND issue.end_date BETWEEN :startDate AND :endDate 
            """
        )

        if (searchIssueRequest.keys.isNotEmpty()) {
            query.append(" AND issue.key NOT IN (:keys) ")
            params["keys"] = searchIssueRequest.keys
        }

        if (searchIssueRequest.estimates.isNotEmpty()) {
            query.append(" AND issue.estimate IN (:estimates) ")
            params["estimates"] = searchIssueRequest.estimates
        }

        if (searchIssueRequest.systems.isNotEmpty()) {
            query.append(" AND issue.system IN (:systems) ")
            params["systems"] = searchIssueRequest.systems
        }

        if (searchIssueRequest.epics.isNotEmpty()) {
            query.append(" AND issue.epic IN (:epics) ")
            params["epics"] = searchIssueRequest.epics
        }

        if (searchIssueRequest.issueTypes.isNotEmpty()) {
            query.append(" AND issue.issue_type IN (:issueTypes) ")
            params["issueTypes"] = searchIssueRequest.issueTypes
        }

        if (searchIssueRequest.projects.isNotEmpty()) {
            query.append(" AND issue.project IN (:projects) ")
            params["projects"] = searchIssueRequest.projects
        }

        if (searchIssueRequest.priorities.isNotEmpty()) {
            query.append(" AND issue.priority IN (:priorities) ")
            params["priorities"] = searchIssueRequest.priorities
        }

        board.dynamicFields?.forEach {
            val values = dynamicFilters[it.name]
            if (!values.isNullOrEmpty()) {
                query.append(" AND issue.dynamic_fields->>'${it.name}' in (:${it.field}) ")
                params[it.field] = values.toList()
            }
        }

        params["boardId"] = board.id
        params["startDate"] = searchIssueRequest.startDate.atStartOfDay()
        params["endDate"] = searchIssueRequest.endDate.atEndOfDay()

        query.append(" ORDER BY issue.key ")

        return jdbcTemplate.query(query.toString(), params, MinimalIssueRowMapper(objectMapper))
    }

    @Transactional(readOnly = true)
    override fun findAllDynamicFieldValues(boardId: Long): List<DynamicFieldsValues> {
        log.info("Method=findAllDynamicFieldValues, boardId={}", boardId)

        val dynamicFields = findAllDynamicFieldsByBoardId(boardId)
        if (dynamicFields.isEmpty()) {
            return emptyList()
        }

        val query =
            """
            SELECT
            ${dynamicFields.joinToString { """ ARRAY_TO_JSON(ARRAY_REMOVE(ARRAY_AGG(DISTINCT fields."$it"), null)) as "$it" """ }}
            FROM issue, JSONB_TO_RECORD(dynamic_fields) AS
            ${dynamicFields.joinToString(prefix = "fields(", postfix = ")") { """ "$it" TEXT """ }}
            WHERE board_id = :boardId
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.queryForObject(query, params, DynamicFieldsValuesRowMapper(objectMapper))
            ?: emptyList()
    }

    override fun findAllEstimatesByBoardId(boardId: Long): Set<String> {
        log.info("M=findAllEstimatesByBoardId, boardId={}", boardId)

        val query =
            """
            SELECT DISTINCT ESTIMATE FROM ISSUE
            WHERE BOARD_ID = :boardId
            AND ESTIMATE IS NOT NULL
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.queryForSet(query, params)
    }

    override fun findAllSystemsByBoardId(boardId: Long): Set<String> {
        log.info("M=findAllSystemsByBoardId, boardId={}", boardId)

        val query =
            """
            SELECT DISTINCT SYSTEM FROM issue
            WHERE BOARD_ID = :boardId
            AND SYSTEM IS NOT NULL
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.queryForSet(query, params)
    }

    override fun findAllEpicsByBoardId(boardId: Long): Set<String> {
        log.info("M=findAllEpicsByBoardId, boardId={}", boardId)

        val query =
            """
            SELECT DISTINCT EPIC FROM issue
            WHERE BOARD_ID = :boardId
            AND EPIC IS NOT NULL
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.queryForSet(query, params)
    }

    override fun findAllIssueTypesByBoardId(boardId: Long): Set<String> {
        log.info("M=findAllIssueTypesByBoardId, boardId={}", boardId)

        val query =
            """
            SELECT DISTINCT ISSUE_TYPE FROM ISSUE
            WHERE BOARD_ID = :boardId
            AND ISSUE_TYPE IS NOT NULL
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.queryForSet(query, params)
    }

    override fun findAllIssueProjectsByBoardId(boardId: Long): Set<String> {
        log.info("M=findAllIssueProjectsByBoardId, boardId={}", boardId)

        val query =
            """
            SELECT DISTINCT PROJECT FROM ISSUE
            WHERE BOARD_ID = :boardId
            AND PROJECT IS NOT NULL
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.queryForSet(query, params)
    }

    override fun findAllIssuePrioritiesByBoardId(boardId: Long): Set<String> {
        log.info("M=findAllIssuePrioritiesByBoardId, boardId={}", boardId)

        val query =
            """
            SELECT DISTINCT PRIORITY FROM ISSUE
            WHERE BOARD_ID = :boardId
            AND PRIORITY IS NOT NULL
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.queryForSet(query, params)
    }

    override fun findAllKeysByBoardIdAndDates(boardId: Long, startDate: LocalDateTime, endDate: LocalDateTime): Set<String> {
        log.info("findAllKeysByBoardIdAndDates, boardId={}, startDate={}, endDate={}", boardId, startDate, endDate)

        val query =
            """
            SELECT DISTINCT KEY FROM ISSUE
            WHERE BOARD_ID = :boardId
            AND END_DATE BETWEEN :startDate AND :endDate
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId
        params["startDate"] = startDate
        params["endDate"] = endDate

        return jdbcTemplate.queryForSet(query, params)
    }

    private fun findAllDynamicFieldsByBoardId(boardId: Long): Set<String> {
        log.info("Method=findAllDynamicFieldsByBoardId, boardId={}", boardId)

        val query =
            """ 
            SELECT DISTINCT JSONB_OBJECT_KEYS(DYNAMIC_FIELDS) 
            FROM ISSUE 
            WHERE BOARD_ID = :boardId
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.queryForSet(query, params)
    }

}
