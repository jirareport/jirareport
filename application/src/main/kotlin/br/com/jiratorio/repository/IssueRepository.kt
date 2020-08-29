package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.Issue
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface IssueRepository : CrudRepository<Issue, Long>, NativeIssueRepository {

    @EntityGraph(attributePaths = ["leadTimes", "impedimentHistory", "columnChangelog"])
    fun findByBoardIdAndId(boardId: Long, id: Long): Issue?

    @EntityGraph(attributePaths = ["leadTimes", "impedimentHistory", "columnChangelog"])
    override fun findById(id: Long): Optional<Issue>

    fun findByIdOrNull(id: Long): Issue? =
        findById(id).orElse(null)

}
