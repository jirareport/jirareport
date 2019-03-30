package br.com.jiratorio.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64

fun String.toBase64(): String =
    String(Base64.getEncoder().encode(this.toByteArray()))

fun String.fromJiraToLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this.substring(0, 19), DateTimeFormatter.ISO_DATE_TIME)
}
