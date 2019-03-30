package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.Issue
import java.time.LocalDate

interface WipService {

    fun calcAvgWip(start: LocalDate, end: LocalDate, issues: List<Issue>, wipColumns: Set<String>): Double

}
