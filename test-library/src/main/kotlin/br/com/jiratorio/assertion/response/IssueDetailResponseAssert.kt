package br.com.jiratorio.assertion.response

import br.com.jiratorio.assertion.BaseAssert
import br.com.jiratorio.domain.entity.ImpedimentHistoryEntity
import br.com.jiratorio.domain.entity.LeadTimeEntity
import br.com.jiratorio.domain.entity.ColumnChangelogEntity
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.response.issue.IssueDetailResponse

class IssueDetailResponseAssert(
    actual: IssueDetailResponse
) : BaseAssert<IssueDetailResponseAssert, IssueDetailResponse>(
    actual,
    IssueDetailResponseAssert::class
) {

    fun hasId(id: Long) = assertAll {
        objects.assertEqual(field("issueDetailResponse.id"), actual.id, id)
    }

    fun hasKey(key: String) = assertAll {
        objects.assertEqual(field("issueDetailResponse.key"), actual.key, key)
    }

    fun hasChangelogSize(columnChangelog: Set<ColumnChangelogEntity>) = assertAll {
        iterables.assertHasSameSizeAs(field("issueDetailResponse.changelog"), actual.changelog, columnChangelog)
    }

    fun hasDueDateHistorySize(dueDateHistory: List<DueDateHistory>?) = assertAll {
        iterables.assertHasSameSizeAs(
            field("issueDetailResponse.dueDateHistory"),
            actual.dueDateHistory,
            dueDateHistory
        )
    }

    fun hasImpedimentHistorySize(impedimentHistory: MutableSet<ImpedimentHistoryEntity>?) = assertAll {
        iterables.assertHasSameSizeAs(
            field("issueDetailResponse.impedimentHistory"),
            actual.impedimentHistory,
            impedimentHistory
        )
    }

    fun hasLeadTimesSize(leadTimes: Set<LeadTimeEntity>?) = assertAll {
        iterables.assertHasSameSizeAs(field("issueDetailResponse.leadTimes"), actual.leadTimes, leadTimes)
    }

    fun hasWaitTime(waitTime: Double) = assertAll {
        objects.assertEqual(field("issueDetailResponse.waitTime"), actual.waitTime, waitTime)
    }

    fun hasTouchTime(touchTime: Double) = assertAll {
        objects.assertEqual(field("issueDetailResponse.touchTime"), actual.touchTime, touchTime)
    }

    fun hasPctEfficiency(pctEfficiency: Double) = assertAll {
        objects.assertEqual(field("issueDetailResponse.pctEfficiency"), actual.pctEfficiency, pctEfficiency)
    }

}

fun IssueDetailResponse.assertThat(assertions: IssueDetailResponseAssert.() -> Unit): IssueDetailResponseAssert =
    IssueDetailResponseAssert(this).assertThat(assertions)
