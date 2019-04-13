package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.extension.time.atEndOfDay
import br.com.jiratorio.service.WeeklyThroughputService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.Locale

@Service
class WeeklyThroughputServiceImpl : WeeklyThroughputService {

    override fun calcWeeklyThroughput(
        startDate: LocalDate,
        endDate: LocalDate,
        issues: List<Issue>
    ): Chart<String, Int> {
        val chart: Chart<String, Int> = Chart()

        val temporalField: TemporalField = WeekFields.of(Locale("pt-BR")).dayOfWeek()
        var currentDate: LocalDate = startDate.with(temporalField, 1)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        while (currentDate.isBefore(endDate)) {
            val startWeek = currentDate
            currentDate = currentDate.plusWeeks(1)
            val endWeek = currentDate

            val issuesCount = issues.count {
                it.endDate in startWeek.atStartOfDay()..endWeek.atEndOfDay()
            }

            chart["[%s - %s]".format(startWeek.format(formatter), endWeek.format(formatter))] = issuesCount
        }

        return chart
    }

}
