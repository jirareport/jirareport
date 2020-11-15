package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.IssuePeriodEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.Optional

@Repository
interface IssuePeriodRepository : CrudRepository<IssuePeriodEntity, Long>, NativeIssuePeriodRepository {

    fun deleteByStartDateAndEndDateAndBoardId(startDate: LocalDate, endDate: LocalDate, boardId: Long)

    @EntityGraph(attributePaths = ["issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig", "columnTimeAverages"])
    fun findByIdAndBoardId(id: Long, boardId: Long): IssuePeriodEntity?

    @EntityGraph(attributePaths = ["issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig", "columnTimeAverages"])
    override fun findById(id: Long): Optional<IssuePeriodEntity>

    fun findByIdOrNull(id: Long): IssuePeriodEntity? =
        findById(id).orElse(null)

}
