package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.Efficiency
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.service.EfficiencyService
import br.com.jiratorio.usecase.efficiency.CalculateEfficiency
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class EfficiencyServiceImpl(
    val calculateEfficiency: CalculateEfficiency
) : EfficiencyService {

    override fun calcEfficiency(
        changelog: List<Changelog>,
        touchingColumns: MutableList<String>?,
        waitingColumns: MutableList<String>?,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Efficiency =
        calculateEfficiency.execute(changelog, touchingColumns, waitingColumns, holidays, ignoreWeekend)

}
