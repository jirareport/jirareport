package br.com.jiratorio.mapper

fun List<String>.toUpperCase(): List<String> =
    this.map { it.toUpperCase() }
