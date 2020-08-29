package br.com.jiratorio.assertion

import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.entity.ColumnChangelog
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import java.time.LocalDateTime

class IssueAssert(
    actual: Issue
) : BaseAssert<IssueAssert, Issue>(
    actual,
    IssueAssert::class
) {

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

    fun hasEstimate(estimate: String?) = assertAll {
        objects.assertEqual(field("issue.estimate"), actual.estimate, estimate)
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

    fun hasColumnChangelog(vararg columnChangelog: ColumnChangelog) = assertAll {
        iterables.assertContains(field("issue.columnChangelog"), actual.columnChangelog, columnChangelog)
    }

    fun hasDeviationOfEstimate(deviationOfEstimate: Long) = assertAll {
        objects.assertEqual(field("issue.deviationOfEstimate"), actual.deviationOfEstimate, deviationOfEstimate)
    }

    fun hasDueDateHistory(dueDateHistory: List<DueDateHistory>) = assertAll {
        objects.assertEqual(field("issue.dueDateHistory"), actual.dueDateHistory, dueDateHistory)
    }

    fun hasImpedimentTime(impedimentTime: Long) = assertAll {
        longs.assertEqual(field("issue.impedimentTime"), actual.impedimentTime, impedimentTime)
    }

    fun hasEmptyImpedimentHistory() = assertAll {
        iterables.assertEmpty(field("issues.impedimentHistory"), actual.impedimentHistory)
    }

    fun containsImpedimentHistory(vararg impedimentHistory: ImpedimentHistory) = assertAll {
        iterables.assertContainsExactly(field("issues.impedimentHistory"), actual.impedimentHistory, impedimentHistory)
    }

    fun hasDynamicFields(dynamicFields: Map<String, String>?) = assertAll {
        objects.assertEqual(field("issue.dynamicFields"), actual.dynamicFields, dynamicFields)
    }

    fun hasEmptyDynamicFields() = assertAll {
        maps.assertEmpty(field("issue.dynamicFields"), actual.dynamicFields)
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

    fun hasLeadTimes(leadTimes: Set<LeadTime>) = assertAll {
        objects.assertEqual(field("issue.leadTimes"), actual.leadTimes, leadTimes)
    }
}

fun Issue.assertThat(assertions: IssueAssert.() -> Unit): IssueAssert =
    IssueAssert(this).assertThat(assertions)
