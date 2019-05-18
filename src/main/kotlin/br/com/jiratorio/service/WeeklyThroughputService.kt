package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import java.time.LocalDate

interface WeeklyThroughputService {

    fun calcWeeklyThroughput(startDate: LocalDate, endDate: LocalDate, issues: List<Issue>): Chart<String, Int>

}
