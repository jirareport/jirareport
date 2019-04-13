package br.com.jiratorio.assert.response

import br.com.jiratorio.assert.BaseAssert
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.response.IssueResponse

class IssueResponseAssert(
    actual: IssueResponse
) : BaseAssert<IssueResponseAssert, IssueResponse>(actual, IssueResponseAssert::class) {
    fun hasId(id: Long) = assertAll {
        objects.assertEqual(field("issueResponse.id"), actual.id, id)
    }

    fun hasKey(key: String) = assertAll {
        objects.assertEqual(field("issueResponse.key"), actual.key, key)
    }

    fun hasCreator(creator: String?) = assertAll {
        objects.assertEqual(field("issueResponse.creator"), actual.creator, creator)
    }

    fun hasSummary(summary: String) = assertAll {
        objects.assertEqual(field("issueResponse.summary"), actual.summary, summary)
    }

    fun hasIssueType(issueType: String?) = assertAll {
        objects.assertEqual(field("issueResponse.issueType"), actual.issueType, issueType)
    }

    fun hasEstimate(estimate: String?) = assertAll {
        objects.assertEqual(field("issueResponse.estimate"), actual.estimate, estimate)
    }

    fun hasProject(project: String?) = assertAll {
        objects.assertEqual(field("issueResponse.project"), actual.project, project)
    }

    fun hasEpic(epic: String?) = assertAll {
        objects.assertEqual(field("issueResponse.epic"), actual.epic, epic)
    }

    fun hasSystem(system: String?) = assertAll {
        objects.assertEqual(field("issueResponse.system"), actual.system, system)
    }

    fun hasPriority(priority: String?) = assertAll {
        objects.assertEqual(field("issueResponse.priority"), actual.priority, priority)
    }

    fun hasLeadTime(leadTime: Long) = assertAll {
        objects.assertEqual(field("issueResponse.leadTime"), actual.leadTime, leadTime)
    }

    fun hasStartDate(startDate: String) = assertAll {
        objects.assertEqual(field("issueResponse.startDate"), actual.startDate, startDate)
    }

    fun hasEndDate(endDate: String) = assertAll {
        objects.assertEqual(field("issueResponse.endDate"), actual.endDate, endDate)
    }

    fun hasCreated(created: String) = assertAll {
        objects.assertEqual(field("issueResponse.created"), actual.created, created)
    }

    fun hasDeviationOfEstimate(deviationOfEstimate: Long?) = assertAll {
        objects.assertEqual(field("issueResponse.deviationOfEstimate"), actual.deviationOfEstimate, deviationOfEstimate)
    }

    fun hasDueDateHistory(dueDateHistory: List<DueDateHistory>?) = assertAll {
        objects.assertEqual(field("issueResponse.dueDateHistory"), actual.dueDateHistory, dueDateHistory)
    }

    fun hasImpedimentTime(impedimentTime: Long) = assertAll {
        objects.assertEqual(field("issueResponse.impedimentTime"), actual.impedimentTime, impedimentTime)
    }

    fun hasDynamicFields(dynamicFields: Map<String, String?>?) = assertAll {
        objects.assertEqual(field("issueResponse.dynamicFields"), actual.dynamicFields, dynamicFields)
    }

    fun hasWaitTime(waitTime: Long) = assertAll {
        objects.assertEqual(field("issueResponse.waitTime"), actual.waitTime, waitTime)
    }

    fun hasTouchTime(touchTime: Long) = assertAll {
        objects.assertEqual(field("issueResponse.touchTime"), actual.touchTime, touchTime)
    }

    fun hasPctEfficiency(pctEfficiency: Double) = assertAll {
        objects.assertEqual(field("issueResponse.pctEfficiency"), actual.pctEfficiency, pctEfficiency)
    }
    
}
