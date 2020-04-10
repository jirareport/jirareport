package br.com.jiratorio.domain.entity.embedded

data class IssueProgression(
    val days: List<String>,
    val issues: Map<String, List<Boolean>>
)