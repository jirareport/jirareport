package br.com.jiratorio.domain.changelog

data class Changelog(
    val fieldChangelog: Set<FieldChangelog> = emptySet(),
    val columnChangelog: Set<ColumnChangelog> = emptySet(),
)
