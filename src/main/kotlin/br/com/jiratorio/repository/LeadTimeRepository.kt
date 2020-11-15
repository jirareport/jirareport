package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.LeadTimeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LeadTimeRepository : CrudRepository<LeadTimeEntity, Long>, NativeLeadTimeRepository {

    fun deleteByIssueId(id: Long)

}
