package br.com.jiratorio.domain

import java.time.LocalDate

interface Holiday {

    val date: LocalDate

    val description: String

}
