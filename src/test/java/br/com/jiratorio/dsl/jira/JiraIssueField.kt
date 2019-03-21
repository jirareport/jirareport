package br.com.jiratorio.dsl.jira

class JiraIssueField(
    val resolution: String? = null,
    val lastViewed: String? = "2019-01-15T19:00:00.000-0300",
    val aggregatetimeoriginalestimate: String? = null,
    val issuelinks: Array<String> = emptyArray(),
    val subtasks: Array<String> = emptyArray(),
    val issuetype: JiraIssueType,

) {
    val bla = """
        "subtasks": [],
        "issuetype": {
          "
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
          "self": "http://localhost:8888/jira/rest/api/2/issue/JIRAT-1/watchers",
          "watchCount": 1,
          "isWatching": false
        },
        "timeoriginalestimate": null,
        "description": "Calcular diferença de data de entrega com o primeiro due date",
        "fixVersions": [
          {
            "self": "http://localhost:8888/jira/rest/api/2/version/1",
            "id": "1",
            "description": "January 2019 release",
            "name": "January-2019",
            "archived": false,
            "released": true,
            "releaseDate": "2019-02-01"
          }
        ],
        "priority": {
          "self": "http://localhost:8888/jira/rest/api/2/priority/1",
          "iconUrl": "http://localhost:8888/images/icons/priorities/major.svg",
          "name": "Major",
          "id": "1"
        },
        "created": "2018-12-25T11:49:35.000-0300",
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
          "self": "http://localhost:8888/jira/rest/api/2/issue/JIRAT-1/votes",
          "votes": 0,
          "hasVoted": false
        },
        "duedate": "2019-01-19",
        "status": {
          "self": "http://localhost:8888/jira/rest/api/2/status/4",
          "description": "Analyse metrics in production",
          "iconUrl": "http://localhost:8888/jira/images/icons/statuses/closed.png",
          "name": "ACCOMPANIMENT",
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
        "summary": "Calcular diferença de data de entrega com o primeiro due date",
        "versions": [],
        "aggregateprogress": {
          "progress": 0,
          "total": 0
        }
    """.trimIndent()
}
