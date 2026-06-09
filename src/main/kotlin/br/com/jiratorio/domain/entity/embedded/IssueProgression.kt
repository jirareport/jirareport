package br.com.jiratorio.domain.entity.embedded

import java.io.Serializable

data class IssueProgression(
    val days: List<String> = emptyList(),
    val issues: Map<String, List<Boolean>> = emptyMap()
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

}
