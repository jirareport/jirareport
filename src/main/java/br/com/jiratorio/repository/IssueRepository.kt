package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.Issue
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface IssueRepository : CrudRepository<Issue, Long>, IssueCustomRepository {

    @Query(
        """
            SELECT DISTINCT estimated FROM Issue
            WHERE board.id = :boardId
            AND estimated IS NOT NULL
        """
    )
    fun findAllEstimatesByBoardId(@Param("boardId") boardId: Long): Set<String>

    @Query(
        """
            SELECT DISTINCT system FROM Issue
            WHERE board.id = :boardId
            AND system IS NOT NULL
            """
    )
    fun findAllSystemsByBoardId(@Param("boardId") boardId: Long): Set<String>

    @Query(
        """
            SELECT DISTINCT epic FROM Issue
            WHERE board.id = :boardId
            AND epic IS NOT NULL
        """
    )
    fun findAllEpicsByBoardId(@Param("boardId") boardId: Long): Set<String>

    @Query(
        """
            SELECT DISTINCT issueType FROM Issue
            WHERE board.id = :boardId
            AND issueType IS NOT NULL
        """
    )
    fun findAllIssueTypesByBoardId(@Param("boardId") boardId: Long): Set<String>

    @Query(
        """
            SELECT DISTINCT project FROM Issue
            WHERE board.id = :boardId
            AND project IS NOT NULL
        """
    )
    fun findAllIssueProjectsByBoardId(@Param("boardId") boardId: Long): Set<String>

    @Query(
        """
           SELECT DISTINCT i FROM Issue i
           JOIN i.issuePeriods ip
           LEFT JOIN FETCH i.leadTimes leadTimes
           LEFT JOIN FETCH leadTimes.leadTimeConfig
           WHERE ip.id = :issuePeriodId
    """
    )
    fun findByIssuePeriodId(@Param("issuePeriodId") issuePeriodId: Long): List<Issue>

    @Query(
        """
            SELECT DISTINCT priority FROM Issue
            WHERE board.id = :boardId
            AND priority IS NOT NULL
        """
    )
    fun findAllIssuePrioritiesByBoardId(@Param("boardId") boardId: Long): Set<String>

}
