package br.com.jiratorio.repository.jdbctemplate

import br.com.jiratorio.domain.DynamicFieldsValues
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.issue.Issue
import br.com.jiratorio.domain.issue.MinimalIssue
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.extension.jdbctemplate.queryForSet
import br.com.jiratorio.extension.time.atEndOfDay
import br.com.jiratorio.repository.NativeIssueRepository
import br.com.jiratorio.repository.jdbctemplate.rowmapper.MinimalIssueRowMapper
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
    jdbcTemplate: JdbcTemplate,
) : NativeIssueRepository {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private val jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    @Transactional(readOnly = true)
    override fun findByExample(
        board: BoardEntity,
        dynamicFilters: Map<String, Array<String>>,
        searchIssueRequest: SearchIssueRequest,
    ): List<Issue> {
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
                   coalesce((SELECT COUNT(*) FROM issue_due_date_history h WHERE h.issue_id = issue.id), 0) as change_estimate_count,
                   issue.impediment_time
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

        board.dynamicFields?.forEachIndexed { i, cfg ->
            val values = dynamicFilters[cfg.name]
            if (!values.isNullOrEmpty()) {
                query.append(
                    " AND EXISTS (SELECT 1 FROM issue_dynamic_field df$i " +
                        "WHERE df$i.issue_id = issue.id AND df$i.field_name = :dfName$i AND df$i.field_value IN (:dfVals$i)) "
                )
                params["dfName$i"] = cfg.name
                params["dfVals$i"] = values.toList()
            }
        }

        params["boardId"] = board.id
        params["startDate"] = searchIssueRequest.startDate.atStartOfDay()
        params["endDate"] = searchIssueRequest.endDate.atEndOfDay()

        query.append(" ORDER BY issue.key ")

        val rawIssues: List<MinimalIssue> = jdbcTemplate.query(query.toString(), params, MinimalIssueRowMapper())

        if (rawIssues.isEmpty()) return rawIssues

        val ids = rawIssues.map { it.id }
        val hydrateQuery =
            """
            SELECT issue_id, ARRAY_AGG(field_name) AS names, ARRAY_AGG(field_value) AS vals
            FROM issue_dynamic_field
            WHERE issue_id IN (:ids)
            GROUP BY issue_id
            """
        val hydrateParams = MapSqlParameterSource()
        hydrateParams["ids"] = ids

        val dynamicMap: Map<Long, Map<String, String>> = jdbcTemplate.query(hydrateQuery, hydrateParams) { rs, _ ->
            val issueId = rs.getLong("issue_id")
            val names = (rs.getArray("names").array as Array<*>).map { it as String }
            val vals = (rs.getArray("vals").array as Array<*>).map { it as String }
            issueId to names.zip(vals).toMap()
        }.toMap()

        return rawIssues.map { it.copy(dynamicFields = dynamicMap[it.id] ?: emptyMap()) }
    }

    @Transactional(readOnly = true)
    override fun findAllDynamicFieldValues(boardId: Long): List<DynamicFieldsValues> {
        log.info("Method=findAllDynamicFieldValues, boardId={}", boardId)

        val query =
            """
            SELECT df.field_name, ARRAY_AGG(DISTINCT df.field_value) AS values
            FROM issue_dynamic_field df JOIN issue i ON i.id = df.issue_id
            WHERE i.board_id = :boardId
            GROUP BY df.field_name
            """

        val params = MapSqlParameterSource()
        params["boardId"] = boardId

        return jdbcTemplate.query(query, params) { rs, _ ->
            val fieldName = rs.getString("field_name")
            val values = (rs.getArray("values").array as Array<*>).map { it as String }
            DynamicFieldsValues(fieldName, values)
        }
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

}
