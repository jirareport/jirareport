package br.com.jiratorio.extension

fun Collection<String>.containsUpperCase(other: String?): Boolean =
    contains(other?.toUpperCase())
