package br.com.jiratorio.extension.decimal

fun Double.zeroIfNaN(): Double {
    return if (this.isNaN()) {
        0.0
    } else {
        this
    }
}

fun Double.format(): String {
    return DecimalFormatter.format(this)
}
