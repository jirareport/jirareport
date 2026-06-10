package br.com.jiratorio.domain.entity.embedded

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Embeddable
data class DueDateHistory(
    @Column(name = "created")
    val created: LocalDateTime?,
    @Column(name = "due_date")
    val dueDate: LocalDate?
) : Serializable {
    companion object {
        private const val serialVersionUID = 7542115783552544574L
    }
}
