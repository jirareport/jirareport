package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.sumByLong

object ImpedimentCalculatorByColumn {

    fun timeInImpediment(changelog: List<Changelog>, impedimentColumns: List<String>): Long {
        return changelog
                .filter { impedimentColumns.contains(it.to) }
                .sumByLong { it.leadTime ?: 0 }
    }

}
