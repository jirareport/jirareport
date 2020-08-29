package br.com.jiratorio.domain.impediment.calculator

import br.com.jiratorio.domain.entity.ImpedimentHistory

data class ImpedimentCalculatorResult(

    val timeInImpediment: Long = 0,

    val impedimentHistory: MutableSet<ImpedimentHistory> = mutableSetOf()

)
