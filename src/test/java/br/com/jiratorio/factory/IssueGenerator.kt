package br.com.jiratorio.factory

fun generate(
    issueKey: String = "JIRAT-1",
    issueId: Int = 1,
    issueDescription: String = "",

    lastView: String = "2019-01-31T23:00:00.000-0300",
    created: String = "2019-01-01T10:00:00.000-0300",

    issueTypeName: String = "Task",
    issueTypeId: Int = 1,

    priorityName: String = "Major",
    priorityId: Int = 1,

    dueDate: String = "2019-01-19",

    customFields: Array<CustomField>
): String {

    return """
        {
          "expand": "operations,versionedRepresentations,editmeta,changelog,renderedFields",
          "id": "$issueId",
          "self": "http://localhost:8888/jira/rest/api/2/issue/$issueId",
          "key": "$issueKey",
          "fields": {
            "resolution": null,
            "lastViewed": "$lastView",
            "aggregatetimeoriginalestimate": null,
            "issuelinks": [],
            "subtasks": [],
            "issuetype": {
              "self": "http://localhost:8888/jira/rest/api/2/issuetype/$issueTypeId",
              "id": "1",
              "description": "",
              "iconUrl": "http://localhost:8888/jira/secure/viewavatar?size=xsmall&avatarId=$issueTypeId&avatarType=issuetype",
              "name": "$issueTypeName",
              "subtask": false,
              "avatarId": $issueTypeId
            },
            "environment": null,
            "timeestimate": null,
            "aggregatetimespent": null,
            "workratio": -1,
            "labels": [],
            "reporter": {
              "self": "http://localhost:8888/jira/rest/api/2/user?username=po.user",
              "name": "po.user",
              "key": "po.user",
              "emailAddress": "po.user@jiratorio.com.br",
              "avatarUrls": {
                "48x48": "http://localhost:8888/jira/secure/useravatar?avatarId=1",
                "24x24": "http://localhost:8888/jira/secure/useravatar?size=small&avatarId=1",
                "16x16": "http://localhost:8888/jira/secure/useravatar?size=xsmall&avatarId=1",
                "32x32": "http://localhost:8888/jira/secure/useravatar?size=medium&avatarId=1"
              },
              "displayName": "PO User",
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
            "priority": {
              "self": "http://localhost:8888/jira/rest/api/2/priority/$priorityId",
              "iconUrl": "http://localhost:8888/images/icons/priorities/$priorityName.svg",
              "name": "$priorityName",
              "id": "$priorityId"
            },
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
              "self": "http://localhost:8888/jira/rest/api/2/status/4",
              "description": "Analyse metrics in production",
              "iconUrl": "http://localhost:8888/jira/images/icons/statuses/closed.png",
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
              "self": "http://localhost:8888/jira/rest/api/2/user?username=po.user",
              "name": "po.user",
              "key": "po.user",
              "emailAddress": "po.user@jiratorio.com.br",
              "avatarUrls": {
                "48x48": "http://localhost:8888/jira/secure/useravatar?avatarId=1",
                "24x24": "http://localhost:8888/jira/secure/useravatar?size=small&avatarId=1",
                "16x16": "http://localhost:8888/jira/secure/useravatar?size=xsmall&avatarId=1",
                "32x32": "http://localhost:8888/jira/secure/useravatar?size=medium&avatarId=1"
              },
              "displayName": "PO User",
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
            },
            ${customFields.joinToString { "$it" }}
          },
          "changelog": {
            "startAt": 0,
            "maxResults": 1,
            "total": 1,
            "histories": [
              {
                "id": "1",
                "author": {
                  "self": "http://localhost:8888/jira/rest/api/2/user?username=po.user",
                  "name": "po.user",
                  "key": "po.user",
                  "emailAddress": "po.user@jiratorio.com.br",
                  "avatarUrls": {
                    "48x48": "http://localhost:8888/secure/useravatar?ownerId=po.user&avatarId=1",
                    "24x24": "http://localhost:8888/secure/useravatar?size=small&ownerId=po.user&avatarId=1",
                    "16x16": "http://localhost:8888/secure/useravatar?size=xsmall&ownerId=po.user&avatarId=1",
                    "32x32": "http://localhost:8888/secure/useravatar?size=medium&ownerId=po.user&avatarId=1"
                  },
                  "displayName": "PO User",
                  "active": true,
                  "timeZone": "America/Sao_Paulo"
                },
                "created": "2018-12-25T11:49:35.000-0300",
                "items": [
                  {
                    "field": "status",
                    "fieldtype": "jira",
                    "from": null,
                    "fromString": null,
                    "to": "1",
                    "toString": "BACKLOG"
                  }
                ]
              },
              {
                "id": "2",
                "author": {
                  "self": "http://localhost:8888/jira/rest/api/2/user?username=po.user",
                  "name": "po.user",
                  "key": "po.user",
                  "emailAddress": "po.user@jiratorio.com.br",
                  "avatarUrls": {
                    "48x48": "http://localhost:8888/secure/useravatar?ownerId=po.user&avatarId=1",
                    "24x24": "http://localhost:8888/secure/useravatar?size=small&ownerId=po.user&avatarId=1",
                    "16x16": "http://localhost:8888/secure/useravatar?size=xsmall&ownerId=po.user&avatarId=1",
                    "32x32": "http://localhost:8888/secure/useravatar?size=medium&ownerId=po.user&avatarId=1"
                  },
                  "displayName": "PO User",
                  "active": true,
                  "timeZone": "America/Sao_Paulo"
                },
                "created": "2019-01-01T10:15:00.000-0300",
                "items": [
                  {
                    "field": "status",
                    "fieldtype": "jira",
                    "from": "2",
                    "fromString": "BACKLOG",
                    "to": "2",
                    "toString": "TODO"
                  }
                ]
              },
              {
                "id": "3",
                "author": {
                  "self": "http://localhost:8888/jira/rest/api/2/user?username=leonardo.ferreira",
                  "name": "leonardo.ferreira",
                  "key": "leonardo.ferreira",
                  "emailAddress": "leonardo.ferreira@jiratorio.com.br",
                  "avatarUrls": {
                    "48x48": "http://localhost:8888/secure/useravatar?ownerId=leonardo.ferreira&avatarId=2",
                    "24x24": "http://localhost:8888/secure/useravatar?size=small&ownerId=leonardo.ferreira&avatarId=2",
                    "16x16": "http://localhost:8888/secure/useravatar?size=xsmall&ownerId=leonardo.ferreira&avatarId=2",
                    "32x32": "http://localhost:8888/secure/useravatar?size=medium&ownerId=leonardo.ferreira&avatarId=2"
                  },
                  "displayName": "Leonardo Ferreira",
                  "active": true,
                  "timeZone": "America/Sao_Paulo"
                },
                "created": "2019-01-01T16:30:00.000-0300",
                "items": [
                  {
                    "field": "status",
                    "fieldtype": "jira",
                    "from": "2",
                    "fromString": "TODO",
                    "to": "3",
                    "toString": "WIP"
                  }
                ]
              },
              {
                "id": "4",
                "author": {
                  "self": "http://localhost:8888/jira/rest/api/2/user?username=leonardo.ferreira",
                  "name": "leonardo.ferreira",
                  "key": "leonardo.ferreira",
                  "emailAddress": "leonardo.ferreira@jiratorio.com.br",
                  "avatarUrls": {
                    "48x48": "http://localhost:8888/secure/useravatar?ownerId=leonardo.ferreira&avatarId=2",
                    "24x24": "http://localhost:8888/secure/useravatar?size=small&ownerId=leonardo.ferreira&avatarId=2",
                    "16x16": "http://localhost:8888/secure/useravatar?size=xsmall&ownerId=leonardo.ferreira&avatarId=2",
                    "32x32": "http://localhost:8888/secure/useravatar?size=medium&ownerId=leonardo.ferreira&avatarId=2"
                  },
                  "displayName": "Leonardo Ferreira",
                  "active": true,
                  "timeZone": "America/Sao_Paulo"
                },
                "created": "2019-01-10T13:45:00.000-0300",
                "items": [
                  {
                    "field": "status",
                    "fieldtype": "jira",
                    "from": "3",
                    "fromString": "WIP",
                    "to": "4",
                    "toString": "ACCOMPANIMENT"
                  }
                ]
              },
              {
                "id": "5",
                "author": {
                  "self": "http://localhost:8888/jira/rest/api/2/user?username=leonardo.ferreira",
                  "name": "leonardo.ferreira",
                  "key": "leonardo.ferreira",
                  "emailAddress": "leonardo.ferreira@jiratorio.com.br",
                  "avatarUrls": {
                    "48x48": "http://localhost:8888/secure/useravatar?ownerId=leonardo.ferreira&avatarId=2",
                    "24x24": "http://localhost:8888/secure/useravatar?size=small&ownerId=leonardo.ferreira&avatarId=2",
                    "16x16": "http://localhost:8888/secure/useravatar?size=xsmall&ownerId=leonardo.ferreira&avatarId=2",
                    "32x32": "http://localhost:8888/secure/useravatar?size=medium&ownerId=leonardo.ferreira&avatarId=2"
                  },
                  "displayName": "Leonardo Ferreira",
                  "active": true,
                  "timeZone": "America/Sao_Paulo"
                },
                "created": "2019-01-15T11:20:00.000-0300",
                "items": [
                  {
                    "field": "status",
                    "fieldtype": "jira",
                    "from": "4",
                    "fromString": "ACCOMPANIMENT",
                    "to": "5",
                    "toString": "DONE"
                  }
                ]
              }
            ]
          }
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

fun main() {
    println(
        generate(
            issueKey = "JIRAT-1",
            issueId = 1,
            issueDescription = "Calcular diferença de data de entrega com o primeiro due date",

            lastView = "2019-01-31T23:00:00.000-0300",
            created = "2019-01-01T10:00:00.000-0300",

            issueTypeName = "Task",
            issueTypeId = 1,

            priorityName = "Major",
            priorityId = 1,

            dueDate = "2019-01-19",

            customFields = arrayOf(
                CustomField(1000, "M"),
                CustomField(2000, "JiraReport"),
                CustomField(3000, "JiraReport Technical debts")
            )
        )
    )
}
