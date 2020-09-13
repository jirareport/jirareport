package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ColumnChangelogRepository : CrudRepository<ColumnChangelogEntity, Long>
