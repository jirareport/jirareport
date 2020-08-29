package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.ImpedimentHistory
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImpedimentHistoryRepository : CrudRepository<ImpedimentHistory, Long>
