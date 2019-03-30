package br.com.jiratorio.service.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.extension.logger
import br.com.jiratorio.extension.time.rangeTo
import br.com.jiratorio.service.WipService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WipServiceImpl : WipService {

    private val log = logger()

    @ExecutionTime
    override fun calcAvgWip(start: LocalDate, end: LocalDate, issues: List<Issue>, wipColumns: Set<String>): Double {
        log.info("Method=calcAvgWip, start={}, end={}, issues={}, wipColumns={}", start, end, issues, wipColumns)

        return (start..end).map { cursor ->
            issues.count { issue ->
                issue.changelog.any { cl ->
                    cursor in cl.dateRange() && cl.to in wipColumns
                }
            }
        }.average()
    }

}
