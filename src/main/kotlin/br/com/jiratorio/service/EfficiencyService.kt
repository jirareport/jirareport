package br.com.jiratorio.service

import br.com.jiratorio.domain.Efficiency
import br.com.jiratorio.domain.entity.embedded.Changelog
import java.time.LocalDate

interface EfficiencyService {

    fun calcEfficiency(
        changelog: List<Changelog>,
        touchingColumns: MutableList<String>?,
        waitingColumns: MutableList<String>?,
        holidays: List<LocalDate>,
        ignoreWeekend: Boolean?
    ): Efficiency

}
