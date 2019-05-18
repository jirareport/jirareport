package br.com.jiratorio.extension

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

fun Date.format(format: String): String =
    SimpleDateFormat(format).format(this)

fun Date.toLocalDateTime(): LocalDateTime =
    this.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

fun Date.toLocalDate(): LocalDate =
    LocalDate.from(
        this.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate()
    )

fun String.toLocalDate(pattern: String): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))

fun String.toLocalDate(): LocalDate =
    this.toLocalDate("dd/MM/yyyy")

fun String.toLocalDateTime(pattern: String): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))

fun String.toLocalDateTime(): LocalDateTime =
    if (this.length == 19)
        this.toLocalDateTime("dd/MM/yyyy HH:mm:ss")
    else if (this.length == 10)
        "$this 00:00".toLocalDateTime("dd/MM/yyyy HH:mm")
    else
        this.toLocalDateTime("dd/MM/yyyy HH:mm")
