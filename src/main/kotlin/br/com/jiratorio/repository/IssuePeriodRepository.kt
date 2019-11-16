package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.entity.IssuePeriod
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface IssuePeriodRepository : CrudRepository<IssuePeriod, Long> {

    @EntityGraph(
        attributePaths = ["issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig"],
        type = EntityGraph.EntityGraphType.LOAD
    )
    fun findByBoardId(boardId: Long): List<IssuePeriod>

    fun deleteByStartDateAndEndDateAndBoardId(
        startDate: LocalDate,
        endDate: LocalDate,
        boardId: Long
    )

    fun findByBoardIdAndId(boardId: Long, id: Long): IssuePeriod?

    @JvmDefault
    fun findByIdOrNull(id: Long): IssuePeriod? =
        findById(id).orElse(null)

}
