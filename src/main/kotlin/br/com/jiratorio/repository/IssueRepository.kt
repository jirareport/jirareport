package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.Issue
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional

@Repository
interface IssueRepository : CrudRepository<Issue, Long>, IssueCustomRepository {

    @Query(
        """
            SELECT DISTINCT estimate FROM Issue
            WHERE board.id = :boardId
            AND estimate IS NOT NULL
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
            SELECT DISTINCT priority FROM Issue
            WHERE board.id = :boardId
            AND priority IS NOT NULL
        """
    )
    fun findAllIssuePrioritiesByBoardId(@Param("boardId") boardId: Long): Set<String>

    @Query(
        """
            SELECT DISTINCT key FROM Issue
            WHERE board.id = :boardId
            AND endDate between :startDate and :endDate
        """
    )
    fun findAllKeysByBoardIdAndDates(
        @Param("boardId") boardId: Long,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): Set<String>

    @EntityGraph(attributePaths = ["leadTimes", "impedimentHistory"])
    fun findByBoardIdAndId(boardId: Long, id: Long): Issue?

    @EntityGraph(attributePaths = ["leadTimes", "impedimentHistory"], type = EntityGraph.EntityGraphType.LOAD)
    override fun findById(id: Long): Optional<Issue>

}
