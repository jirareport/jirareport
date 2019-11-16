package br.com.jiratorio.assert.response

import br.com.jiratorio.assert.BaseAssert
import br.com.jiratorio.domain.entity.ImpedimentHistory
import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.entity.embedded.Changelog
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

    fun hasChangelogSize(changelog: List<Changelog>) = assertAll {
        iterables.assertHasSameSizeAs(field("issueDetailResponse.changelog"), actual.changelog, changelog)
    }

    fun hasDueDateHistorySize(dueDateHistory: List<DueDateHistory>?) = assertAll {
        iterables.assertHasSameSizeAs(
            field("issueDetailResponse.dueDateHistory"),
            actual.dueDateHistory,
            dueDateHistory
        )
    }

    fun hasImpedimentHistorySize(impedimentHistory: MutableSet<ImpedimentHistory>?) = assertAll {
        iterables.assertHasSameSizeAs(
            field("issueDetailResponse.impedimentHistory"),
            actual.impedimentHistory,
            impedimentHistory
        )
    }

    fun hasLeadTimesSize(leadTimes: Set<LeadTime>?) = assertAll {
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
