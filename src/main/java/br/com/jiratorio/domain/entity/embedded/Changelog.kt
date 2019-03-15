package br.com.jiratorio.domain.entity.embedded

import java.io.Serializable
import java.time.LocalDateTime

data class Changelog(
        var from: String? = null,
        var to: String? = null,
        var created: LocalDateTime? = null,
        var leadTime: Long? = null,
        var endDate: LocalDateTime? = null
) : Serializable {
    companion object {
        private const val serialVersionUID = 6432821107545516780L
    }
}
