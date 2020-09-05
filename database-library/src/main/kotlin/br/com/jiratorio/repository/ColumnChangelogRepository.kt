package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ColumnChangelogRepository : JpaRepository<ColumnChangelogEntity, Long>
