package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.embedded.Changelog

object ImpedimentCalculatorByColumn {

    fun timeInImpediment(changelog: List<Changelog>, impedimentColumns: List<String>) =
        changelog
            .filter { impedimentColumns.contains(it.to) }
            .map { it.leadTime ?: 0 }
            .sum()

}
