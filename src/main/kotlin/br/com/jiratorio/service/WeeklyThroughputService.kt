package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.issue.Issue
import br.com.jiratorio.extension.time.atEndOfDay
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.Locale

@Service
class WeeklyThroughputService {

    private val log = LoggerFactory.getLogger(javaClass)

    fun calculate(startDate: LocalDate, endDate: LocalDate, issues: List<Issue>): Chart<String, Int> {
        log.info("startDate={}, endDate={}, issues={}", startDate, endDate, issues)

        val chart: Chart<String, Int> = Chart()

        val temporalField: TemporalField = WeekFields.of(Locale("pt-BR")).dayOfWeek()
        var currentDate: LocalDate = startDate.with(temporalField, 1)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        while (currentDate.isBefore(endDate)) {
            val startWeek = currentDate
            currentDate = currentDate.plusWeeks(1)
            val endWeek = currentDate

            val throughput = issues.count {
                it.endDate in startWeek.atStartOfDay()..endWeek.atEndOfDay()
            }

            chart["[%s - %s]".format(startWeek.format(formatter), endWeek.format(formatter))] = throughput
        }

        return chart
    }

}
