package br.com.jiratorio.extension.decimal

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object DecimalFormatter {
    private val symbols: DecimalFormatSymbols = DecimalFormatSymbols()
    private val formatter: DecimalFormat = DecimalFormat("##0.00")

    init {
        symbols.decimalSeparator = '.'
        formatter.decimalFormatSymbols = symbols
    }

    fun format(d: Double): String {
        return formatter.format(d)
    }
}
