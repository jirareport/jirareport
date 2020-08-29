package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.IssuePeriod
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.Optional

@Repository
interface IssuePeriodRepository : CrudRepository<IssuePeriod, Long> {

    @Query(
        value = """
            SELECT DISTINCT issuePeriod
            FROM IssuePeriod issuePeriod
            LEFT JOIN FETCH issuePeriod.columnTimeAverages   
            LEFT JOIN FETCH issuePeriod.issues issues
            LEFT JOIN FETCH issues.leadTimes leadTimes
            LEFT JOIN FETCH leadTimes.leadTimeConfig
            WHERE issuePeriod.board.id = :boardId
            AND issuePeriod.startDate >= :startDate
            AND issuePeriod.endDate <= :endDate
            ORDER BY issuePeriod.startDate 
        """
    )
    fun findAll(
        @Param("boardId") boardId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<IssuePeriod>

    fun deleteByStartDateAndEndDateAndBoardId(
        startDate: LocalDate,
        endDate: LocalDate,
        boardId: Long
    )

    @EntityGraph(attributePaths = ["issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig", "columnTimeAverages"])
    fun findByIdAndBoardId(id: Long, boardId: Long): IssuePeriod?

    @EntityGraph(attributePaths = ["issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig", "columnTimeAverages"])
    override fun findById(id: Long): Optional<IssuePeriod>

    fun findByIdOrNull(id: Long): IssuePeriod? =
        findById(id).orElse(null)

}
