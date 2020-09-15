package br.com.jiratorio.assertion.response

import br.com.jiratorio.assertion.error.ShouldBeEquals
import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import br.com.jiratorio.domain.entity.LeadTimeEntity
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import org.assertj.core.api.AbstractAssert
import org.assertj.core.error.ShouldHaveSize.shouldHaveSize

class IssueDetailResponseAssert private constructor(
    actual: IssueDetailResponse,
) : AbstractAssert<IssueDetailResponseAssert, IssueDetailResponse>(
    actual,
    IssueDetailResponseAssert::class.java
) {

    fun hasId(id: Long): IssueDetailResponseAssert {
        if (actual.id != id) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.id, id).create())
        }

        return this
    }

    fun hasKey(key: String): IssueDetailResponseAssert {
        if (actual.key != key) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.key, key).create())
        }

        return this
    }

    fun hasChangelogSize(columnChangelog: Set<ColumnChangelogEntity>): IssueDetailResponseAssert {
        if (actual.changelog.size != columnChangelog.size) {
            failWithMessage(shouldHaveSize(actual.changelog, actual.changelog.size, columnChangelog.size).create())
        }

        return this
    }

    fun hasDueDateHistorySize(dueDateHistory: List<DueDateHistory>?): IssueDetailResponseAssert {
        if (actual.dueDateHistory?.size != dueDateHistory?.size) {
            failWithMessage(shouldHaveSize(actual.dueDateHistory, actual.dueDateHistory?.size ?: 0, dueDateHistory?.size ?: 0).create())
        }

        return this
    }

    fun hasImpedimentHistorySize(impedimentHistory: MutableSet<ImpedimentHistoryEntity>?): IssueDetailResponseAssert {
        if (actual.impedimentHistory.size != impedimentHistory?.size) {
            failWithMessage(shouldHaveSize(actual.impedimentHistory, actual.impedimentHistory.size, impedimentHistory?.size ?: 0).create())
        }

        return this
    }

    fun hasLeadTimesSize(leadTimes: Set<LeadTimeEntity>?): IssueDetailResponseAssert {
        if (actual.leadTimes?.size != leadTimes?.size) {
            failWithMessage(shouldHaveSize(actual.leadTimes, actual.leadTimes?.size ?: 0, leadTimes?.size ?: 0).create())
        }

        return this
    }

    fun hasWaitTime(waitTime: Double): IssueDetailResponseAssert {
        if (actual.waitTime != waitTime) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.waitTime, waitTime).create())
        }

        return this
    }

    fun hasTouchTime(touchTime: Double): IssueDetailResponseAssert {
        if (actual.touchTime != touchTime) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.touchTime, touchTime).create())
        }

        return this
    }

    fun hasPctEfficiency(pctEfficiency: Double): IssueDetailResponseAssert {
        if (actual.pctEfficiency != pctEfficiency) {
            failWithMessage(ShouldBeEquals.shouldBeEquals(actual.pctEfficiency, pctEfficiency).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: IssueDetailResponse): IssueDetailResponseAssert =
            IssueDetailResponseAssert(actual)

    }

}
