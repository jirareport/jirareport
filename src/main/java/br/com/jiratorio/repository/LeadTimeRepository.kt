package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.LeadTime
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LeadTimeRepository : CrudRepository<LeadTime, Long> {

    fun deleteByIssueId(id: Long?)

}
