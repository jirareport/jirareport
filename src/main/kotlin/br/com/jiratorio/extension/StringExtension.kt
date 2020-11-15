package br.com.jiratorio.extension

import java.text.Normalizer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64

fun String.toBase64(): String =
    Base64.getEncoder().encodeToString(this.toByteArray())

fun String.fromJiraToLocalDateTime(): LocalDateTime =
    substring(0, 19)
        .replace("T", " ")
        .toLocalDateTime()

fun String.stripAccents(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")

fun String?.isPresent() =
    !isNullOrBlank()

fun String.toLocalDate(pattern: String = "dd/MM/yyyy"): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))

fun String.toLocalDateTime(formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")): LocalDateTime =
    LocalDateTime.parse(this, formatter)
