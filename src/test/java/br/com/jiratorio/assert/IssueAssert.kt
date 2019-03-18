package br.com.jiratorio.assert

import br.com.jiratorio.domain.DynamicFieldsValues
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import java.time.LocalDateTime

class IssueAssert(actual: Issue) :
        BaseAssert<IssueAssert, Issue>(actual, IssueAssert::class) {

    fun hasKey(key: String) = assertAll {
        objects.assertEqual(field("issue.key"), actual.key, key)
    }

    fun hasIssueType(issueType: String) = assertAll {
        objects.assertEqual(field("issue.issueType"), actual.issueType, issueType)
    }

    fun hasCreator(creator: String) = assertAll {
        objects.assertEqual(field("issue.creator"), actual.creator, creator)
    }

    fun hasSystem(system: String?) = assertAll {
        objects.assertEqual(field("issue.key"), actual.system, system)
    }

    fun hasEpic(epic: String?) = assertAll {
        objects.assertEqual(field("issue.epic"), actual.epic, epic)
    }

    fun hasSummary(summary: String) = assertAll {
        objects.assertEqual(field("issue.summary"), actual.summary, summary)
    }

    fun hasEstimated(estimated: String?) = assertAll {
        objects.assertEqual(field("issue.estimated"), actual.estimated, null)
    }

    fun hasProject(project: String?) = assertAll {
        objects.assertEqual(field("issue.project"), actual.project, project)
    }

    fun hasStartDate(startDate: LocalDateTime) = assertAll {
        objects.assertEqual(field("issue.startDate"), actual.startDate, startDate)
    }

    fun hasEndDate(endDate: LocalDateTime) = assertAll {
        objects.assertEqual(field("issue.endDate"), actual.endDate, endDate)
    }

    fun hasLeadTime(leadTime: Long) = assertAll {
        longs.assertEqual(field("issue.leadTime"), actual.leadTime, leadTime)
    }

    fun hasCreated(created: LocalDateTime) = assertAll {
        objects.assertEqual(field("issue.created"), actual.created, created)
    }

    fun hasPriority(priority: String) = assertAll {
        objects.assertEqual(field("issue.priority"), actual.priority, priority)
    }

    fun hasChangelog(vararg changelog: Changelog) = assertAll {
        iterables.assertContains(field("issue.changelog"), actual.changelog, changelog)
    }

    fun hasDeviationOfEstimate(deviationOfEstimate: String?) = assertAll {
        objects.assertEqual(field("issue.deviationOfEstimate"), actual.deviationOfEstimate, deviationOfEstimate)
    }

    fun hasDueDateHistory(dueDateHistory: List<DueDateHistory>?) = assertAll {
        objects.assertEqual(field("issue.dueDateHistory"), actual.dueDateHistory, dueDateHistory)
    }

    fun hasImpedimentTime(impedimentTime: Long) = assertAll {
        longs.assertEqual(field("issue.impedimentTime"), actual.impedimentTime, impedimentTime)
    }

    fun hasDynamicFields(dynamicFields: Map<String, String>?) = assertAll {
        objects.assertEqual(field("issue.dynamicFields"), actual.dynamicFields, dynamicFields)
    }

    fun hasWaitTime(waitTime: Long) = assertAll {
        longs.assertEqual(field("issue.waitTime"), actual.waitTime, waitTime)
    }

    fun hasTouchTime(touchTime: Long) = assertAll {
        longs.assertEqual(field("issue.touchTime"), actual.touchTime, touchTime)
    }

    fun hasPctEfficiency(pctEfficiency: Double) = assertAll {
        objects.assertEqual(field("issue.pctEfficiency"), actual.pctEfficiency, pctEfficiency)
    }

}
