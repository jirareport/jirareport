package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.Issue
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface IssueRepository : CrudRepository<Issue, Long>, IssueCustomRepository {

    @Query(
        value = """
            SELECT DISTINCT issue.estimated FROM issue
            WHERE issue.board_id = :boardId
            AND issue.estimated IS NOT NULL
            """,
        nativeQuery = true
    )
    fun findAllEstimativesByBoardId(@Param("boardId") boardId: Long?): List<String>

    @Query(
        value = """
            SELECT DISTINCT issue.system FROM issue
            WHERE issue.board_id = :boardId
            AND issue.system IS NOT NULL
            """,
        nativeQuery = true
    )
    fun findAllSystemsByBoardId(@Param("boardId") boardId: Long?): List<String>

    @Query(
        value = """
            SELECT DISTINCT issue.epic FROM issue
            WHERE issue.board_id = :boardId
            AND issue.epic IS NOT NULL
            """,
        nativeQuery = true
    )
    fun findAllEpicsByBoardId(@Param("boardId") boardId: Long?): List<String>

    @Query(
        value = """
            SELECT DISTINCT issue.issue_type FROM issue
            WHERE issue.board_id = :boardId
            AND issue.issue_type IS NOT NULL
            """,
        nativeQuery = true
    )
    fun findAllIssueTypesByBoardId(@Param("boardId") boardId: Long?): List<String>

    @Query(
        value = """
            SELECT DISTINCT issue.project FROM issue
            WHERE issue.board_id = :boardId
            AND issue.project IS NOT NULL
            """,
        nativeQuery = true
    )
    fun findAllIssueProjectsByBoardId(@Param("boardId") boardId: Long?): List<String>

    @Query(
        """
           SELECT DISTINCT i FROM Issue i
           JOIN i.issuePeriods ip
           LEFT JOIN FETCH i.leadTimes leadTimes
           LEFT JOIN FETCH leadTimes.leadTimeConfig
           WHERE ip.id = :issuePeriodId
    """
    )
    fun findByIssuePeriodId(@Param("issuePeriodId") issuePeriodId: Long?): List<Issue>

    @Query(
        value = """
            SELECT DISTINCT issue.priority FROM issue
            WHERE issue.board_id = :boardId
            AND issue.priority IS NOT NULL
            """,
        nativeQuery = true
    )
    fun findAllIssuePrioritiesByBoardId(@Param("boardId") boardId: Long?): List<String>

}
