package br.com.jiratorio.assert.response

import br.com.jiratorio.assert.BaseAssert
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodResponse

class IssuePeriodResponseAssert(actual: IssuePeriodResponse) :
    BaseAssert<IssuePeriodResponseAssert, IssuePeriodResponse>(actual, IssuePeriodResponseAssert::class) {

    fun hasId(id: Long) = assertAll {
        objects.assertEqual(field("issuePeriodResponse.id"), actual.id, id)
    }

    fun hasDates(dates: String) = assertAll {
        objects.assertEqual(field("issuePeriodResponse.dates"), actual.dates, dates)
    }

    fun hasWipAvg(wipAvg: Double) = assertAll {
        objects.assertEqual(field("issuePeriodResponse.wipAvg"), actual.wipAvg, wipAvg)
    }

    fun hasLeadTime(leadTime: Double) = assertAll {
        objects.assertEqual(field("issuePeriodResponse.leadTime"), actual.leadTime, leadTime)
    }

    fun hasAvgPctEfficiency(avgPctEfficiency: Double) = assertAll {
        objects.assertEqual(field("issuePeriodResponse.avgPctEfficiency"), actual.avgPctEfficiency, avgPctEfficiency)
    }

    fun hasJql(jql: String) = assertAll {
        objects.assertEqual(field("issuePeriodResponse.jql"), actual.jql, jql)
    }

    fun hasThroughput(throughput: Int) = assertAll {
        objects.assertEqual(field("issuePeriodResponse.throughput"), actual.throughput, throughput)
    }

}
