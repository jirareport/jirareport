package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.IssuePeriod
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.Optional

@Repository
interface IssuePeriodRepository : CrudRepository<IssuePeriod, Long> {

    @EntityGraph(attributePaths = ["issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig", "columnTimeAverages"])
    fun findByBoardId(boardId: Long): List<IssuePeriod>

    fun deleteByStartDateAndEndDateAndBoardId(
        startDate: LocalDate,
        endDate: LocalDate,
        boardId: Long
    )

    @EntityGraph(attributePaths = ["issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig", "columnTimeAverages"])
    fun findByIdAndBoardId(id: Long, boardId: Long): IssuePeriod?

    @EntityGraph(attributePaths = ["issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig", "columnTimeAverages"])
    override fun findById(id: Long): Optional<IssuePeriod>

    @JvmDefault
    fun findByIdOrNull(id: Long): IssuePeriod? =
        findById(id).orElse(null)

}
