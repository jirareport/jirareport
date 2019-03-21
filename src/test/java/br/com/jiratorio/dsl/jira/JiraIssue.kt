package br.com.jiratorio.dsl.jira

class JiraIssue(
    val id: Int,
    val key: String
) {

    val expand = "operations,versionedRepresentations,editmeta,changelog,renderedFields"

    val self = "http://localhost:8888/jira/rest/api/2/issue/$id"

    var fields: Array<JiraIssueField> = emptyArray()

    val bla = """
        {
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
        }"""

}
