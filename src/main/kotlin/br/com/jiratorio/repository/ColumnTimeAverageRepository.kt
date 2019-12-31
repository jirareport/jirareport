package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.ColumnTimeAverage
import org.springframework.data.jpa.repository.JpaRepository

interface ColumnTimeAverageRepository : JpaRepository<ColumnTimeAverage, Long>
