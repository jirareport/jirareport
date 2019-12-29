package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.service.WipService
import br.com.jiratorio.usecase.wip.CalculateAverageWip
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WipServiceImpl(
    private val calculateAverageWip: CalculateAverageWip
) : WipService {

    override fun calcAvgWip(start: LocalDate, end: LocalDate, issues: List<Issue>, wipColumns: Set<String>): Double =
        calculateAverageWip.execute(start, end, issues, wipColumns)

}
