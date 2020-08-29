package br.com.jiratorio.domain.impediment

import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculator
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorByColumn
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorByFlag

enum class ImpedimentType(calculator: ImpedimentCalculator) : ImpedimentCalculator by calculator {
    COLUMN(ImpedimentCalculatorByColumn),
    FLAG(ImpedimentCalculatorByFlag);
}
