package br.com.jiratorio.extension

fun List<String>?.toUpperCase(): List<String>? {
    return this?.map { it.toUpperCase() }
}
