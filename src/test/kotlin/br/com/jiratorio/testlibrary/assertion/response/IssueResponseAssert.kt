package br.com.jiratorio.testlibrary.assertion.response

import br.com.jiratorio.testlibrary.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.response.issue.IssueResponse
import org.assertj.core.api.AbstractAssert

class IssueResponseAssert private constructor(
    actual: IssueResponse,
) : AbstractAssert<IssueResponseAssert, IssueResponse>(
    actual,
    IssueResponseAssert::class.java
) {

    fun hasId(id: Long): IssueResponseAssert {
        if (actual.id != id) {
            failWithMessage(shouldBeEquals(actual.id, id).create())
        }

        return this
    }

    fun hasKey(key: String): IssueResponseAssert {
        if (actual.key != key) {
            failWithMessage(shouldBeEquals(actual.key, key).create())
        }

        return this
    }

    fun hasCreator(creator: String?): IssueResponseAssert {
        if (actual.creator != creator) {
            failWithMessage(shouldBeEquals(actual.creator, creator).create())
        }

        return this
    }

    fun hasSummary(summary: String): IssueResponseAssert {
        if (actual.summary != summary) {
            failWithMessage(shouldBeEquals(actual.summary, summary).create())
        }

        return this
    }

    fun hasIssueType(issueType: String?): IssueResponseAssert {
        if (actual.issueType != issueType) {
            failWithMessage(shouldBeEquals(actual.issueType, issueType).create())
        }

        return this
    }

    fun hasEstimate(estimate: String?): IssueResponseAssert {
        if (actual.estimate != estimate) {
            failWithMessage(shouldBeEquals(actual.estimate, estimate).create())
        }

        return this
    }

    fun hasProject(project: String?): IssueResponseAssert {
        if (actual.project != project) {
            failWithMessage(shouldBeEquals(actual.project, project).create())
        }

        return this
    }

    fun hasEpic(epic: String?): IssueResponseAssert {
        if (actual.epic != epic) {
            failWithMessage(shouldBeEquals(actual.epic, epic).create())
        }

        return this
    }

    fun hasSystem(system: String?): IssueResponseAssert {
        if (actual.system != system) {
            failWithMessage(shouldBeEquals(actual.system, system).create())
        }

        return this
    }

    fun hasPriority(priority: String?): IssueResponseAssert {
        if (actual.priority != priority) {
            failWithMessage(shouldBeEquals(actual.priority, priority).create())
        }

        return this
    }

    fun hasLeadTime(leadTime: Long): IssueResponseAssert {
        if (actual.leadTime != leadTime) {
            failWithMessage(shouldBeEquals(actual.leadTime, leadTime).create())
        }

        return this
    }

    fun hasStartDate(startDate: String): IssueResponseAssert {
        if (actual.startDate != startDate) {
            failWithMessage(shouldBeEquals(actual.startDate, startDate).create())
        }

        return this
    }

    fun hasEndDate(endDate: String): IssueResponseAssert {
        if (actual.endDate != endDate) {
            failWithMessage(shouldBeEquals(actual.endDate, endDate).create())
        }

        return this
    }

    fun hasCreated(created: String): IssueResponseAssert {
        if (actual.created != created) {
            failWithMessage(shouldBeEquals(actual.created, created).create())
        }

        return this
    }

    fun hasDeviationOfEstimate(deviationOfEstimate: Long?): IssueResponseAssert {
        if (actual.deviationOfEstimate != deviationOfEstimate) {
            failWithMessage(shouldBeEquals(actual.deviationOfEstimate, deviationOfEstimate).create())
        }

        return this
    }

    fun hasChangeEstimateCount(changeEstimateCount: Int?): IssueResponseAssert {
        if (actual.changeEstimateCount != changeEstimateCount) {
            failWithMessage(shouldBeEquals(actual.changeEstimateCount, changeEstimateCount).create())
        }

        return this
    }

    fun hasImpedimentTime(impedimentTime: Long): IssueResponseAssert {
        if (actual.impedimentTime != impedimentTime) {
            failWithMessage(shouldBeEquals(actual.impedimentTime, impedimentTime).create())
        }

        return this
    }

    fun hasDynamicFields(dynamicFields: Map<String, String?>?): IssueResponseAssert {
        if (actual.dynamicFields != dynamicFields) {
            failWithMessage(shouldBeEquals(actual.dynamicFields, dynamicFields).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: IssueResponse): IssueResponseAssert =
            IssueResponseAssert(actual)

    }

}
