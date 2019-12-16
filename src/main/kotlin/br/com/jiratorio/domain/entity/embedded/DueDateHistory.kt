package br.com.jiratorio.domain.entity.embedded

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class DueDateHistory(

    val created: LocalDateTime?,

    val dueDate: LocalDate?

) : Serializable {
    companion object {
        private const val serialVersionUID = 7542115783552544574L
    }
}
