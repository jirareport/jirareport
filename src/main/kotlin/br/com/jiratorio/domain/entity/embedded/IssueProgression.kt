package br.com.jiratorio.domain.entity.embedded

data class IssueProgression(
    val days: List<String> = emptyList(),
    val issues: Map<String, List<Boolean>> = emptyMap()
)
