package br.com.jiratorio.integration.field

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.config.junit.testtype.IntegrationTest
import br.com.jiratorio.dsl.restAssured
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test

@IntegrationTest
internal class FieldIntegrationTest(
    private val authenticator: Authenticator
) {

    @Test
    @LoadStubs(["/fields"])
    fun `find all fields`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/fields")
            }
            then {
                statusCode(SC_OK)
                body(
                    "collect { it.id }",
                    containsInAnyOrder(
                        "issuetype",
                        "customfield_11080",
                        "timespent",
                        "customfield_10150",
                        "project",
                        "fixVersions",
                        "aggregatetimespent",
                        "resolution",
                        "resolutiondate",
                        "workratio",
                        "lastViewed",
                        "watches",
                        "thumbnail",
                        "customfield_10180",
                        "customfield_10181",
                        "created",
                        "customfield_10182",
                        "customfield_10380",
                        "customfield_10381",
                        "customfield_10140",
                        "customfield_10580",
                        "customfield_10142",
                        "customfield_10781",
                        "customfield_10143",
                        "priority",
                        "customfield_10782",
                        "customfield_10783",
                        "customfield_10981",
                        "labels",
                        "timeestimate",
                        "aggregatetimeoriginalestimate",
                        "versions",
                        "issuelinks",
                        "assignee",
                        "updated",
                        "status",
                        "components",
                        "issuekey",
                        "customfield_10170",
                        "customfield_10171",
                        "timeoriginalestimate",
                        "description",
                        "timetracking",
                        "security",
                        "attachment",
                        "aggregatetimeestimate",
                        "summary",
                        "creator",
                        "subtasks",
                        "customfield_10280",
                        "customfield_10480",
                        "customfield_10680",
                        "reporter",
                        "customfield_10681",
                        "customfield_10120",
                        "aggregateprogress",
                        "customfield_10682",
                        "customfield_10880",
                        "customfield_10000",
                        "customfield_10683",
                        "customfield_10002",
                        "customfield_10684",
                        "environment",
                        "duedate",
                        "progress",
                        "comment",
                        "votes",
                        "worklog"

                    )
                )
                body(
                    "collect { it.name }",
                    containsInAnyOrder(
                        "Issue Type",
                        "Development",
                        "Time Spent",
                        "Release Version History",
                        "Project",
                        "Fix Version/s",
                        "Σ Time Spent",
                        "Resolution",
                        "Resolved",
                        "Work Ratio",
                        "Last Viewed",
                        "Watchers",
                        "Images",
                        "Days since last comment",
                        "Last commented by a User",
                        "Created",
                        "Last updater",
                        "Rank (Obsolete)",
                        "Ranking",
                        "Flagged",
                        "issueFunction",
                        "Story Points",
                        "Actual Story Points",
                        "Business Value",
                        "Priority",
                        "Acceptance Criteria",
                        "Out of Scope",
                        "Spring Forum Reference",
                        "Labels",
                        "Remaining Estimate",
                        "Σ Original Estimate",
                        "Affects Version/s",
                        "Linked Issues",
                        "Assignee",
                        "Updated",
                        "Status",
                        "Component/s",
                        "Key",
                        "First Response Date",
                        "Time in Status",
                        "Original Estimate",
                        "Description",
                        "Time Tracking",
                        "Security Level",
                        "Attachment",
                        "Σ Remaining Estimate",
                        "Summary",
                        "Creator",
                        "Sub-Tasks",
                        "Global Rank",
                        "Sprint",
                        "Epic Link",
                        "Reporter",
                        "Epic Name",
                        "Reference URL",
                        "Σ Progress",
                        "Epic Status",
                        "Rank",
                        "Virtual Machine",
                        "Epic Colour",
                        "Platform",
                        "Pull Request URL",
                        "Environment",
                        "Due Date",
                        "Progress",
                        "Comment",
                        "Votes",
                        "Log Work"
                    )
                )
            }
        }

    }
}
