package br.com.jiratorio.extension

fun <T> buildList(block: MutableList<T>.() -> Unit): List<T> =
    mutableListOf<T>()
        .also { block(it) }
        .toList()
