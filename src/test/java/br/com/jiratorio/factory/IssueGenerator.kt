fun generate(vararg issueData: IssueData): String {
    val issues = issueData.map {
        with(it) {
            """
                    {
                      "expand": "operations,versionedRepresentations,editmeta,changelog,renderedFields",
                      "id": "$issueId",
                      "self": "http://localhost:8888/jira/rest/api/2/issue/$issueId",
                      "key": "$issueKey",
                      "fields": {
                        "resolution": null,
                        "lastViewed": "${histories.last().created}",
                        "updated": "${histories.last().created}",
                        "aggregatetimeoriginalestimate": null,
                        "issuelinks": [],
                        "subtasks": [],
                        $issueType,
                        "environment": null,
                        "timeestimate": null,
                        "aggregatetimespent": null,
                        "workratio": -1,
                        "labels": [],
                        "reporter": {
                          "self": "http://localhost:8888/jira/rest/api/2/user?username=leonardo.ferreira",
                          "name": "leonardo.ferreira",
                          "key": "leonardo.ferreira",
                          "emailAddress": "leonardo.ferreira@jiratorio.com.br",
                          "avatarUrls": {
                            "48x48": "http://localhost:8888/jira/secure/useravatar?avatarId=1",
                            "24x24": "http://localhost:8888/jira/secure/useravatar?size=small&avatarId=1",
                            "16x16": "http://localhost:8888/jira/secure/useravatar?size=xsmall&avatarId=1",
                            "32x32": "http://localhost:8888/jira/secure/useravatar?size=medium&avatarId=1"
                          },
                          "displayName": "Leonardo Ferreira",
                          "active": true,
                          "timeZone": "America/Sao_Paulo"
                        },
                        "watches": {
                          "self": "http://localhost:8888/jira/rest/api/2/issue/$issueKey/watchers",
                          "watchCount": 1,
                          "isWatching": false
                        },
                        "timeoriginalestimate": null,
                        "description": "$issueDescription",
                        "fixVersions": null,
                        $priority,
                        "created": "$created",
                        "assignee": {
                          "self": "http://localhost:8888/jira/rest/api/2/user?username=leonardo.ferreira",
                          "name": "leonardo.ferreira",
                          "key": "leonardo.ferreira",
                          "emailAddress": "leonardo.ferreira@jiratorio.com.br",
                          "avatarUrls": {
                            "48x48": "http://localhost:8888/jira/secure/useravatar?ownerId=leonardo.ferreira&avatarId=2",
                            "24x24": "http://localhost:8888/jira/secure/useravatar?size=small&ownerId=leonardo.ferreira&avatarId=2",
                            "16x16": "http://localhost:8888/jira/secure/useravatar?size=xsmall&ownerId=leonardo.ferreira&avatarId=2",
                            "32x32": "http://localhost:8888/jira/secure/useravatar?size=medium&ownerId=leonardo.ferreira&avatarId=2"
                          },
                          "displayName": "Leonardo Ferreira",
                          "active": true,
                          "timeZone": "America/Sao_Paulo"
                        },
                        "votes": {
                          "self": "http://localhost:8888/jira/rest/api/2/issue/$issueKey/votes",
                          "votes": 0,
                          "hasVoted": false
                        },
                        "duedate": "$dueDate",
                        "status": {
                          "self": "http://localhost:8888/jira/rest/api/2/status/99",
                          "description": "DONE",
                          "iconUrl": "http://localhost:8888/jira/images/icons/statuses/done.png",
                          "name": "DONE",
                          "id": "5",
                          "statusCategory": {
                            "self": "http://localhost:8888/jira/rest/api/2/statuscategory/1",
                            "id": 1,
                            "key": "done",
                            "colorName": "green",
                            "name": "Done"
                          }
                        },
                        "aggregatetimeestimate": null,
                        "creator": {
                          "self": "http://localhost:8888/jira/rest/api/2/user?username=leonardo.ferreira",
                          "name": "leonardo.ferreira",
                          "key": "leonardo.ferreira",
                          "emailAddress": "leonardo.ferreira@jiratorio.com.br",
                          "avatarUrls": {
                            "48x48": "http://localhost:8888/jira/secure/useravatar?avatarId=1",
                            "24x24": "http://localhost:8888/jira/secure/useravatar?size=small&avatarId=1",
                            "16x16": "http://localhost:8888/jira/secure/useravatar?size=xsmall&avatarId=1",
                            "32x32": "http://localhost:8888/jira/secure/useravatar?size=medium&avatarId=1"
                          },
                          "displayName": "Leonardo Ferreira",
                          "active": true,
                          "timeZone": "America/Sao_Paulo"
                        },
                        "timespent": null,
                        "components": [],
                        "progress": {
                          "progress": 0,
                          "total": 0
                        },
                        "project": {
                          "self": "http://localhost:8888/jira/rest/api/2/project/1",
                          "id": "1",
                          "key": "JIRAT",
                          "name": "JiraReport",
                          "avatarUrls": {
                            "48x48": "http://localhost:8888/jira/secure/projectavatar?pid=1&avatarId=1",
                            "24x24": "http://localhost:8888/jira/secure/projectavatar?size=small&pid=1&avatarId=1",
                            "16x16": "http://localhost:8888/jira/secure/projectavatar?size=xsmall&pid=1&avatarId=1",
                            "32x32": "http://localhost:8888/jira/secure/projectavatar?size=medium&pid=1&avatarId=1"
                          }
                        },
                        "resolutiondate": null,
                        "summary": "$issueDescription",
                        "versions": [],
                        "aggregateprogress": {
                          "progress": 0,
                          "total": 0
                        }
                        ${if (customFields.isNotEmpty()) "," else ""}
                        ${customFields.joinToString()}
                      },
                      "changelog": {
                        "startAt": 0,
                        "maxResults": ${histories.size},
                        "total": ${histories.size},
                        "histories": [
                          ${histories.joinToString()}
                        ]
                      }
                    }
                        """.trimIndent()
        }
    }

    return """
        {
          "expand": "schema,names,changelog",
          "startAt": 0,
          "maxResults": 100,
          "total": ${issues.size},
          "issues": [
            ${issues.joinToString()}
          ]
        }
    """.trimIndent()
}

data class CustomField(
    val id: Int,
    val value: String
) {
    override fun toString(): String {
        return """
            "customfield_$id": {
              "self": "http://localhost:8888/jira/rest/api/2/customFieldOption/$id",
              "value": "$value",
              "id": "$id"
            }
        """.trimIndent()
    }
}

data class HistoryItem(
    val field: String = "status",
    val fieldType: String = "jira",
    val from: String?,
    val fromString: String?,
    val to: String?,
    val toString: String?
) {

    override fun toString(): String {
        return """
            {
                "field": "$field",
                "fieldtype": "$fieldType",
                "from": ${if (from == null) null else """ "$from" """},
                "fromString": ${if (fromString == null) null else """ "$fromString" """},
                "to": ${if (to == null) null else """ "$to" """},
                "toString": ${if (toString == null) null else """ "$toString" """}
              }
        """.trimIndent()
    }
}

data class History(
    val id: Int,
    val created: String,
    val items: Array<HistoryItem>
) {
    override fun toString(): String {
        return """
            {
                "id": "$id",
                "author": {
                  "self": "http://localhost:8888/jira/rest/api/2/user?username=leonardo.ferreira",
                  "name": "leonardo.ferreira",
                  "key": "leonardo.ferreira",
                  "emailAddress": "leonardo.ferreira@jiratorio.com.br",
                  "avatarUrls": {
                    "48x48": "http://localhost:8888/secure/useravatar?ownerId=leonardo.ferreira&avatarId=1",
                    "24x24": "http://localhost:8888/secure/useravatar?size=small&ownerId=leonardo.ferreira&avatarId=1",
                    "16x16": "http://localhost:8888/secure/useravatar?size=xsmall&ownerId=leonardo.ferreira&avatarId=1",
                    "32x32": "http://localhost:8888/secure/useravatar?size=medium&ownerId=leonardo.ferreira&avatarId=1"
                  },
                  "displayName": "Leonardo Ferreira",
                  "active": true,
                  "timeZone": "America/Sao_Paulo"
                },
                "created": "$created",
                "items": [
                  ${items.joinToString()}
                ]
              }
        """.trimIndent()
    }
}

data class IssueType(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return """
        "issuetype": {
            "self": "http://localhost:8888/jira/rest/api/2/issuetype/$id",
            "id": "$id",
            "description": "",
            "iconUrl": "http://localhost:8888/jira/secure/viewavatar?size=xsmall&avatarId=$id&avatarType=issuetype",
            "name": "$name",
            "subtask": false,
            "avatarId": $id
        }
        """
    }
}

data class Priority(
    val id: Int,
    val name: String
) {

    override fun toString(): String {
        return """
            "priority": {
              "self": "http://localhost:8888/jira/rest/api/2/priority/$id",
              "iconUrl": "http://localhost:8888/images/icons/priorities/$name.svg",
              "name": "$name",
              "id": "$id"
            }
        """.trimIndent()
    }
}

data class IssueData(
    val issueId: Int,
    val issueDescription: String,
    val created: String,
    val issueType: IssueType,
    val priority: Priority,
    val dueDate: String,
    val customFields: Array<CustomField> = emptyArray(),
    val histories: Array<History> = emptyArray()
) {
    val issueKey: String
        get() = "JIRAT-$issueId"
}

// fun main() {
//    basicIssues()
// }

fun basicIssues() {
    val task = IssueType(1, "Task")
    val major = Priority(1, "Major")

    println(
        generate(
            IssueData(
                issueId = 1,
                issueDescription = "Calcular diferença de data de entrega com o primeiro due date",

                created = "2018-12-25T11:49:35.000-0300",

                issueType = task,
                priority = major,

                dueDate = "2019-01-19",

                histories = arrayOf(
                    History(
                        id = 1,
                        created = "2018-12-25T11:49:35.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = null,
                                fromString = null,
                                to = "1",
                                toString = "BACKLOG"
                            )
                        )
                    ),
                    History(
                        id = 2,
                        created = "2019-01-01T10:15:00.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = "2",
                                fromString = "BACKLOG",
                                to = "2",
                                toString = "TODO"
                            )
                        )
                    ),
                    History(
                        id = 3,
                        created = "2019-01-01T16:30:00.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = "2",
                                fromString = "TODO",
                                to = "3",
                                toString = "WIP"
                            )
                        )
                    ),
                    History(
                        id = 4,
                        created = "2019-01-10T13:45:00.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = "3",
                                fromString = "WIP",
                                to = "4",
                                toString = "ACCOMPANIMENT"
                            )
                        )
                    ),
                    History(
                        id = 5,
                        created = "2019-01-15T11:20:00.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = "4",
                                fromString = "ACCOMPANIMENT",
                                to = "99",
                                toString = "DONE"
                            )
                        )
                    )
                )
            ),
            IssueData(
                issueId = 2,
                issueDescription = "Filtro por campo dinâmico",

                created = "2019-01-05T14:00:35.000-0300",

                issueType = task,
                priority = major,

                dueDate = "2019-01-31",

                histories = arrayOf(
                    History(
                        id = 1,
                        created = "2019-01-05T14:00:35.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = null,
                                fromString = null,
                                to = "1",
                                toString = "BACKLOG"
                            )
                        )
                    ),
                    History(
                        id = 2,
                        created = "2019-01-08T16:15:00.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = "2",
                                fromString = "BACKLOG",
                                to = "2",
                                toString = "TODO"
                            )
                        )
                    ),
                    History(
                        id = 3,
                        created = "2019-01-10T15:03:00.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = "2",
                                fromString = "TODO",
                                to = "3",
                                toString = "WIP"
                            )
                        )
                    ),
                    History(
                        id = 4,
                        created = "2019-01-26T17:45:00.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = "3",
                                fromString = "WIP",
                                to = "4",
                                toString = "ACCOMPANIMENT"
                            )
                        )
                    ),
                    History(
                        id = 5,
                        created = "2019-01-31T10:12:00.000-0300",
                        items = arrayOf(
                            HistoryItem(
                                from = "4",
                                fromString = "ACCOMPANIMENT",
                                to = "99",
                                toString = "DONE"
                            )
                        )
                    )
                )
            )
        )
    )
}
