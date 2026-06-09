package br.com.jiratorio.domain.request

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SearchJiraIssueRequest(
    val jql: String,
    val maxResults: Int,
    val nextPageToken: String? = null,
    val fields: List<String> = listOf("*all"),
    val expand: String = "changelog",
)
