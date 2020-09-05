package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.IssueEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface IssueRepository : CrudRepository<IssueEntity, Long>, NativeIssueRepository {

    @EntityGraph(attributePaths = ["leadTimes", "impedimentHistory", "columnChangelog"])
    fun findByBoardIdAndId(boardId: Long, id: Long): IssueEntity?

    @EntityGraph(attributePaths = ["leadTimes", "impedimentHistory", "columnChangelog"])
    override fun findById(id: Long): Optional<IssueEntity>

    fun findByIdOrNull(id: Long): IssueEntity? =
        findById(id).orElse(null)

}
