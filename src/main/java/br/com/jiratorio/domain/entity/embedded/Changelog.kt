package br.com.jiratorio.domain.entity.embedded

import br.com.jiratorio.extension.LocalDateProgression
import br.com.jiratorio.extension.rangeTo
import java.io.Serializable
import java.time.LocalDateTime

data class Changelog(
    var from: String? = null,
    var to: String? = null,
    var created: LocalDateTime,
    var endDate: LocalDateTime = created,
    var leadTime: Long = 0
) : Serializable {
    companion object {
        private const val serialVersionUID = 6432821107545516780L
    }

    fun dateRange(): LocalDateProgression {
        return created.toLocalDate()..endDate.toLocalDate()
    }
}
