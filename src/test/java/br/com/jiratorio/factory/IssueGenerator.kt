import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

fun generate(issueData: Array<IssueData>): String {
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
    val customFields: MutableList<CustomField> = mutableListOf(),
    val histories: MutableList<History> = mutableListOf()
) {
    var dueDate: String = "2019-01-01"

    val issueKey: String
        get() = "JIRAT-$issueId"
}

fun main2() {
    val task = IssueType(1, "Task")
    val attendance = IssueType(1, "Attendance")
    val subTask = IssueType(2, "SubTask")

    val major = Priority(1, "Major")
    val expedite = Priority(2, "Expedite")
    val medium = Priority(2, "Medium")

    val epicSandBox = CustomField(1000, "SandBox")
    val epicPeriod = CustomField(1000, "Period")

    val estimateP = CustomField(2000, "P")
    val estimateM = CustomField(2000, "M")
    val estimateG = CustomField(2000, "G")

    val systemJiraReport = CustomField(3000, "JiraReport")
    val systemJiraWeb = CustomField(3000, "JiraWeb")

    val projectMetric = CustomField(4000, "Metric")
    val projectEstimate = CustomField(4000, "Estimate")

    val dynamicFieldTeamA = CustomField(5000, "Team A")
    val dynamicFieldTeamB = CustomField(5000, "Team B")
    val dynamicFieldTeamC = CustomField(5000, "Team C")

    val dynamicFieldLevelOfDependency1 = CustomField(6000, "1")
    val dynamicFieldLevelOfDependency2 = CustomField(6000, "2")
    val dynamicFieldLevelOfDependency3 = CustomField(6000, "3")
    val dynamicFieldLevelOfDependency4 = CustomField(6000, "3")
    val dynamicFieldLevelOfDependency5 = CustomField(6000, "3")

    val issues = arrayOf(
        IssueData(
            issueId = 1,
            issueDescription = "Calcular diferença de data de entrega com o primeiro due date",

            created = "2019-01-01T12:00:00.000-0300",

            issueType = task,
            priority = major,

            customFields = mutableListOf(
                epicPeriod,
                estimateP,
                systemJiraReport,
                projectMetric,
                dynamicFieldTeamA,
                dynamicFieldLevelOfDependency1
            )
        ),
        IssueData(
            issueId = 2,
            issueDescription = "Calcular diferença de data de entrega com o primeiro due date",

            created = "2019-01-02T12:10:00.000-0300",

            issueType = task,
            priority = major,

            customFields = mutableListOf(
                epicPeriod,
                estimateP,
                systemJiraReport,
                projectMetric,
                dynamicFieldTeamA,
                dynamicFieldLevelOfDependency2
            )
        ),
        IssueData(
            issueId = 3,
            issueDescription = "Permitir escolher tipo do gráfico",

            created = "2019-01-03T12:20:00.000-0300",

            issueType = task,
            priority = major,

            customFields = mutableListOf(
                epicPeriod,
                estimateP,
                systemJiraReport,
                projectMetric,
                dynamicFieldTeamA,
                dynamicFieldLevelOfDependency3
            )
        ),
        IssueData(
            issueId = 4,
            issueDescription = "Criar opção de adicionar campos dinâmicos para extração de métricas",

            created = "2019-01-04T12:30:00.000-0300",

            issueType = task,
            priority = expedite,

            customFields = mutableListOf(
                epicPeriod,
                estimateM,
                systemJiraReport,
                projectMetric,
                dynamicFieldTeamA,
                dynamicFieldLevelOfDependency4
            )
        ),
        IssueData(
            issueId = 5,
            issueDescription = "Calcular diferença de data de entrega com o primeiro due date",

            created = "2019-01-05T12:40:00.000-0300",

            issueType = task,
            priority = expedite,

            customFields = mutableListOf(
                epicSandBox,
                estimateM,
                systemJiraReport,
                projectMetric,
                dynamicFieldTeamB,
                dynamicFieldLevelOfDependency5
            )
        ),
        IssueData(
            issueId = 6,
            issueDescription = "Tratar prioridade como um campo opcional",

            created = "2019-01-06T12:50:00.000-0300",

            issueType = attendance,
            priority = expedite,

            customFields = mutableListOf(
                epicSandBox,
                estimateM,
                systemJiraWeb,
                projectMetric,
                dynamicFieldTeamB,
                dynamicFieldLevelOfDependency1
            )
        ),
        IssueData(
            issueId = 7,
            issueDescription = "Alterar lead time quando tem issues vinculadas",

            created = "2019-01-07T13:00:00.000-0300",

            issueType = attendance,
            priority = medium,

            customFields = mutableListOf(
                epicSandBox,
                estimateG,
                systemJiraWeb,
                projectMetric,
                dynamicFieldTeamB,
                dynamicFieldLevelOfDependency2
            )
        ),
        IssueData(
            issueId = 8,
            issueDescription = "Correção do GuiChart (parar de usar set)",

            created = "2019-01-08T13:10:00.000-0300",

            issueType = attendance,
            priority = medium,

            customFields = mutableListOf(
                epicSandBox,
                estimateG,
                systemJiraWeb,
                projectEstimate,
                dynamicFieldTeamC,
                dynamicFieldLevelOfDependency3
            )
        ),
        IssueData(
            issueId = 9,
            issueDescription = "Adicionar opção de begin at 0 nos gráficos",

            created = "2019-01-09T13:20:00.000-0300",

            issueType = subTask,
            priority = medium,

            customFields = mutableListOf(
                epicPeriod,
                estimateP,
                systemJiraWeb,
                projectEstimate,
                dynamicFieldTeamC,
                dynamicFieldLevelOfDependency4
            )
        ),
        IssueData(
            issueId = 10,
            issueDescription = "Calcular WIP Médio",

            created = "2019-01-10T13:30:00.000-0300",

            issueType = subTask,
            priority = medium,

            customFields = mutableListOf(
                epicSandBox,
                estimateM,
                systemJiraWeb,
                projectEstimate,
                dynamicFieldTeamC,
                dynamicFieldLevelOfDependency5
            )
        )
    )

    val random = Random(System.currentTimeMillis())
    val addDay: (str: String) -> String = {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(it.substring(0, 10), formatter)
            .plusDays(random.nextLong(5) + 1)
            .format(formatter) + "T12:00:00.000-0300"
    }

    val addDayStartZero: (str: String) -> String = {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(it.substring(0, 10), formatter)
            .plusDays(random.nextLong(5))
            .format(formatter)
    }

    issues.forEach {
        val backlog = History(
            id = 1,
            created = addDay(it.created),
            items = arrayOf(
                HistoryItem(
                    from = null,
                    fromString = null,
                    to = "1",
                    toString = "BACKLOG"
                )
            )
        )

        val analysis = History(
            id = 2,
            created = addDay(backlog.created),
            items = arrayOf(
                HistoryItem(
                    from = "1",
                    fromString = "BACKLOG",
                    to = "2",
                    toString = "ANALYSIS"
                )
            )
        )

        val devWip = History(
            id = 3,
            created = addDay(analysis.created),
            items = arrayOf(
                HistoryItem(
                    from = "2",
                    fromString = "ANALYSIS",
                    to = "3",
                    toString = "DEV WIP"
                )
            )
        )

        val devDone = History(
            id = 4,
            created = addDay(devWip.created),
            items = arrayOf(
                HistoryItem(
                    from = "3",
                    fromString = "DEV WIP",
                    to = "4",
                    toString = "DEV DONE"
                )
            )
        )

        val testWip = History(
            id = 5,
            created = addDay(devDone.created),
            items = arrayOf(
                HistoryItem(
                    from = "4",
                    fromString = "DEV DONE",
                    to = "5",
                    toString = "TEST WIP"
                )
            )
        )

        val testDone = History(
            id = 6,
            created = addDay(testWip.created),
            items = arrayOf(
                HistoryItem(
                    from = "5",
                    fromString = "TEST WIP",
                    to = "6",
                    toString = "TEST DONE"
                )
            )
        )

        val review = History(
            id = 7,
            created = addDay(testDone.created),
            items = arrayOf(
                HistoryItem(
                    from = "6",
                    fromString = "TEST DONE",
                    to = "7",
                    toString = "REVIEW"
                )
            )
        )

        val accompaniment = History(
            id = 8,
            created = addDay(review.created),
            items = arrayOf(
                HistoryItem(
                    from = "7",
                    fromString = "REVIEW",
                    to = "8",
                    toString = "ACCOMPANIMENT"
                )
            )
        )

        val done = History(
            id = 9,
            created = addDay(accompaniment.created),
            items = arrayOf(
                HistoryItem(
                    from = "8",
                    fromString = "ACCOMPANIMENT",
                    to = "99",
                    toString = "DONE"
                )
            )
        )

        it.dueDate = addDayStartZero(it.created)

        val dueDates = mutableListOf<History>()
        for (i in 1..random.nextInt(1, 3)) {
            val from: String? = if (dueDates.isEmpty()) {
                null
            } else {
                dueDates.last().items.first().to
            }

            if (false) {
                println(i)
            }

            val to: String = if (dueDates.isEmpty()) {
                it.dueDate.substring(0, 10)
            } else {
                addDayStartZero(from!!)
            }

            val created: String = if (dueDates.isEmpty()) {
                addDay(it.created)
            } else {
                addDay(dueDates.last().created)
            }

            dueDates.add(
                History(
                    id = 10,
                    created = created,
                    items = arrayOf(
                        HistoryItem(
                            field = "duedate",
                            from = from,
                            fromString = from,
                            to = to,
                            toString = to
                        )
                    )
                )
            )
        }

        it.histories.addAll(arrayOf(backlog, analysis, devWip, devDone, testWip, testDone, review, accompaniment, done))
        it.histories.addAll(dueDates)
    }

    println(generate(issues))
}
