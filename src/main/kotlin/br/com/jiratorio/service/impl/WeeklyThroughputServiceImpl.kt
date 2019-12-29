package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.service.WeeklyThroughputService
import br.com.jiratorio.usecase.weeklythroughput.CalculateWeeklyThroughput
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WeeklyThroughputServiceImpl(
    private val calculateWeeklyThroughput: CalculateWeeklyThroughput
) : WeeklyThroughputService {

    override fun calcWeeklyThroughput(
        startDate: LocalDate,
        endDate: LocalDate,
        issues: List<Issue>
    ): Chart<String, Int> =
        calculateWeeklyThroughput.execute(startDate, endDate, issues)

}
