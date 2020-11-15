package br.com.jiratorio.testlibrary.assertion

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.LeadTimeEntity
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import org.assertj.core.api.AbstractAssert
import org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty
import org.assertj.core.error.ShouldContain.shouldContain
import java.time.LocalDateTime

class IssueAssert(
    actual: IssueEntity,
) : AbstractAssert<IssueAssert, IssueEntity>(
    actual,
    IssueAssert::class.java
) {

    fun hasKey(key: String): IssueAssert {
        if (actual.key != key) {
            failWithMessage(shouldBeEquals(actual.key, key).create())
        }

        return this
    }

    fun hasIssueType(issueType: String): IssueAssert {
        if (actual.issueType != issueType) {
            failWithMessage(shouldBeEquals(actual.issueType, issueType).create())
        }

        return this
    }

    fun hasCreator(creator: String): IssueAssert {
        if (actual.creator != creator) {
            failWithMessage(shouldBeEquals(actual.creator, creator).create())
        }

        return this
    }

    fun hasSystem(system: String?): IssueAssert {
        if (actual.system != system) {
            failWithMessage(shouldBeEquals(actual.system, system).create())
        }

        return this
    }

    fun hasEpic(epic: String?): IssueAssert {
        if (actual.epic != epic) {
            failWithMessage(shouldBeEquals(actual.epic, epic).create())
        }

        return this
    }

    fun hasSummary(summary: String): IssueAssert {
        if (actual.summary != summary) {
            failWithMessage(shouldBeEquals(actual.summary, summary).create())
        }

        return this
    }

    fun hasEstimate(estimate: String?): IssueAssert {
        if (actual.estimate != estimate) {
            failWithMessage(shouldBeEquals(actual.estimate, estimate).create())
        }

        return this
    }

    fun hasProject(project: String?): IssueAssert {
        if (actual.project != project) {
            failWithMessage(shouldBeEquals(actual.project, project).create())
        }

        return this
    }

    fun hasStartDate(startDate: LocalDateTime): IssueAssert {
        if (actual.startDate != startDate) {
            failWithMessage(shouldBeEquals(actual.startDate, startDate).create())
        }

        return this
    }

    fun hasEndDate(endDate: LocalDateTime): IssueAssert {
        if (actual.endDate != endDate) {
            failWithMessage(shouldBeEquals(actual.endDate, endDate).create())
        }

        return this
    }

    fun hasLeadTime(leadTime: Long): IssueAssert {
        if (actual.leadTime != leadTime) {
            failWithMessage(shouldBeEquals(actual.leadTime, leadTime).create())
        }

        return this
    }

    fun hasCreated(created: LocalDateTime): IssueAssert {
        if (actual.created != created) {
            failWithMessage(shouldBeEquals(actual.created, created).create())
        }

        return this
    }

    fun hasPriority(priority: String): IssueAssert {
        if (actual.priority != priority) {
            failWithMessage(shouldBeEquals(actual.priority, priority).create())
        }

        return this
    }

    fun hasColumnChangelog(vararg columnChangelog: ColumnChangelogEntity): IssueAssert {
        val notFound = columnChangelog.filter { !actual.columnChangelog.contains(it) }

        if (notFound.isNotEmpty()) {
            failWithMessage(shouldContain(actual.columnChangelog, columnChangelog, notFound).create())
        }

        return this
    }

    fun hasDeviationOfEstimate(deviationOfEstimate: Long): IssueAssert {
        if (actual.deviationOfEstimate != deviationOfEstimate) {
            failWithMessage(shouldBeEquals(actual.deviationOfEstimate, deviationOfEstimate).create())
        }

        return this
    }

    fun hasDueDateHistory(dueDateHistory: List<DueDateHistory>): IssueAssert {
        if (actual.dueDateHistory != dueDateHistory) {
            failWithMessage(shouldBeEquals(actual.dueDateHistory, dueDateHistory).create())
        }

        return this
    }

    fun hasImpedimentTime(impedimentTime: Long): IssueAssert {
        if (actual.impedimentTime != impedimentTime) {
            failWithMessage(shouldBeEquals(actual.impedimentTime, impedimentTime).create())
        }

        return this
    }

    fun hasEmptyImpedimentHistory(): IssueAssert {
        if (actual.impedimentHistory.isNotEmpty()) {
            failWithMessage(shouldBeEmpty(actual.impedimentHistory).create())
        }

        return this
    }

    fun containsImpedimentHistory(vararg impedimentHistory: ImpedimentHistoryEntity): IssueAssert {
        val notFound = impedimentHistory.filter { !actual.impedimentHistory.contains(it) }

        if (notFound.isNotEmpty()) {
            failWithMessage(shouldContain(actual.impedimentHistory, impedimentHistory, notFound).create())
        }

        return this
    }

    fun hasDynamicFields(dynamicFields: Map<String, String>?): IssueAssert {
        if (actual.dynamicFields != dynamicFields) {
            failWithMessage(shouldBeEquals(actual.dynamicFields, dynamicFields).create())
        }

        return this
    }

    fun hasEmptyDynamicFields(): IssueAssert {
        if (actual.dynamicFields.isNotEmpty()) {
            failWithMessage(shouldBeEmpty(actual.dynamicFields).create())
        }

        return this
    }

    fun hasWaitTime(waitTime: Long): IssueAssert {
        if (actual.waitTime != waitTime) {
            failWithMessage(shouldBeEquals(actual.waitTime, waitTime).create())
        }

        return this
    }

    fun hasTouchTime(touchTime: Long): IssueAssert {
        if (actual.touchTime != touchTime) {
            failWithMessage(shouldBeEquals(actual.touchTime, touchTime).create())
        }

        return this
    }

    fun hasPctEfficiency(pctEfficiency: Double): IssueAssert {
        if (actual.pctEfficiency != pctEfficiency) {
            failWithMessage(shouldBeEquals(actual.pctEfficiency, pctEfficiency).create())
        }

        return this
    }

    fun hasLeadTimes(leadTimes: Set<LeadTimeEntity>): IssueAssert {
        if (actual.leadTimes != leadTimes) {
            failWithMessage(shouldBeEquals(actual.leadTimes, leadTimes).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: IssueEntity): IssueAssert =
            IssueAssert(actual)

    }

}
