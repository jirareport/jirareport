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
interface IssueRepository : CrudRepository<Issue, Long>, NativeIssueRepository {

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

    @EntityGraph(attributePaths = ["leadTimes", "impedimentHistory", "columnChangelog"])
    fun findByBoardIdAndId(boardId: Long, id: Long): Issue?

    @EntityGraph(attributePaths = ["leadTimes", "impedimentHistory", "columnChangelog"])
    override fun findById(id: Long): Optional<Issue>

    @JvmDefault
    fun findByIdOrNull(id: Long): Issue? =
        findById(id).orElse(null)

}
