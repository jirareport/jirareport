package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.ColumnTimeAverageEntity
import org.springframework.data.repository.CrudRepository

interface ColumnTimeAverageRepository : CrudRepository<ColumnTimeAverageEntity, Long>, NativeColumnTimeAverageRepository
