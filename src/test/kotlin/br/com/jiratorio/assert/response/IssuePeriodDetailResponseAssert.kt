package br.com.jiratorio.assert.response

import br.com.jiratorio.assert.BaseAssert
import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodDetailResponse

class IssuePeriodDetailResponseAssert(
    actual: IssuePeriodDetailResponse
) : BaseAssert<IssuePeriodDetailResponseAssert, IssuePeriodDetailResponse>(
    actual,
    IssuePeriodDetailResponseAssert::class
) {

    fun hasDates(dates: String) = assertAll {
        objects.assertEqual(field("issuePeriodDetailResponse.dates"), actual.dates, dates)
    }

    fun hasLeadTime(leadTime: Double) = assertAll {
        objects.assertEqual(field("issuePeriodDetailResponse.leadTime"), actual.leadTime, leadTime)
    }

    fun hasThroughput(throughput: Int) = assertAll {
        objects.assertEqual(field("issuePeriodDetailResponse.throughput"), actual.throughput, throughput)
    }

    fun hasLeadTimeByEstimate(leadTimeByEstimate: Chart<String, Double>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.leadTimeByEstimate"),
            actual.leadTimeByEstimate,
            leadTimeByEstimate
        )
    }

    fun hasThroughputByEstimate(throughputByEstimate: Chart<String, Int>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.throughputByEstimate"),
            actual.throughputByEstimate,
            throughputByEstimate
        )
    }

    fun hasLeadTimeBySystem(leadTimeBySystem: Chart<String, Double>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.leadTimeBySystem"),
            actual.leadTimeBySystem,
            leadTimeBySystem
        )
    }

    fun hasThroughputBySystem(throughputBySystem: Chart<String, Int>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.throughputBySystem"),
            actual.throughputBySystem,
            throughputBySystem
        )
    }

    fun hasLeadTimeByType(leadTimeByType: Chart<String, Double>?) = assertAll {
        objects.assertEqual(field("issuePeriodDetailResponse.leadTimeByType"), actual.leadTimeByType, leadTimeByType)
    }

    fun hasThroughputByType(throughputByType: Chart<String, Int>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.throughputByType"),
            actual.throughputByType,
            throughputByType
        )
    }

    fun hasLeadTimeByProject(leadTimeByProject: Chart<String, Double>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.leadTimeByProject"),
            actual.leadTimeByProject,
            leadTimeByProject
        )
    }

    fun hasThroughputByProject(throughputByProject: Chart<String, Int>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.throughputByProject"),
            actual.throughputByProject,
            throughputByProject
        )
    }

    fun hasLeadTimeByPriority(leadTimeByPriority: Chart<String, Double>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.leadTimeByPriority"),
            actual.leadTimeByPriority,
            leadTimeByPriority
        )
    }

    fun hasThroughputByPriority(throughputByPriority: Chart<String, Int>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.throughputByPriority"),
            actual.throughputByPriority,
            throughputByPriority
        )
    }

    fun hasColumnTimeAvg(columnTimeAvg: MutableList<ColumnTimeAvg>?) = assertAll {
        objects.assertEqual(field("issuePeriodDetailResponse.columnTimeAvg"), actual.columnTimeAvg, columnTimeAvg)
    }

    fun hasLeadTimeCompareChart(leadTimeCompareChart: Chart<String, Double>?) = assertAll {
        objects.assertEqual(
            field("issuePeriodDetailResponse.leadTimeCompareChart"),
            actual.leadTimeCompareChart,
            leadTimeCompareChart
        )
    }

    fun hasDynamicCharts(dynamicCharts: MutableList<DynamicChart>?) = assertAll {
        objects.assertEqual(field("issuePeriodDetailResponse.dynamicCharts"), actual.dynamicCharts, dynamicCharts)
    }
}

fun IssuePeriodDetailResponse.assertThat(assertions: IssuePeriodDetailResponseAssert.() -> Unit): IssuePeriodDetailResponseAssert =
    IssuePeriodDetailResponseAssert(this).assertThat(assertions)
