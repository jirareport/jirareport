package br.com.jiratorio.extension

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.concurrent.TimeUnit

fun Date.format(format: String) =
        SimpleDateFormat(format).format(this)

fun Date.toLocalDateTime() =
        this.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

fun Date.toLocalDate() =
        LocalDate.from(this.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate())
