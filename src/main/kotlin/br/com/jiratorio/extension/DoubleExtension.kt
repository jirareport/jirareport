package br.com.jiratorio.extension

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Double.zeroIfNaN(): Double {
    return if (this.isNaN()) {
        0.0
    } else {
        this
    }
}

fun Double.format(): String {
    return CustomFormatter.format(this)
}

object CustomFormatter {
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
