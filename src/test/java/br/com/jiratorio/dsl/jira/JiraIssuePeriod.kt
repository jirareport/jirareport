package br.com.jiratorio.dsl.jira

class JiraIssuePeriod(
    val issues: Array<JiraIssue>
) {

    val expand: String = "schema,names"

    val startAt: Int = 0

    val maxResults: Int = 100

    val total
        get() = issues.size

}
