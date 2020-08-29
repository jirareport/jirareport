package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.ColumnTimeAverage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface ColumnTimeAverageRepository : CrudRepository<ColumnTimeAverage, Long>
