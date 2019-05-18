package br.com.jiratorio.assert

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.IssuePeriod
import java.time.LocalDate

class IssuePeriodAssert(actual: IssuePeriod) :
    BaseAssert<IssuePeriodAssert, IssuePeriod>(actual, IssuePeriodAssert::class) {

    fun hasStartDate(startDate: LocalDate) = assertAll {
        objects.assertEqual(field("issuePeriod.startDate"), actual.startDate, startDate)
    }

    fun hasEndDate(endDate: LocalDate) = assertAll {
        objects.assertEqual(field("issuePeriod.endDate"), actual.endDate, endDate)
    }

    fun hasLeadTime(leadTime: Double) = assertAll {
        objects.assertEqual(field("issuePeriod.leadTime"), actual.leadTime, leadTime)
    }

    fun histogram() =
        HistogramAssert(actual.histogram!!)

    fun hasLeadTimeByEstimate(vararg leadTimeByEstimate: Pair<String, Double>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.leadTimeByEstimate"),
            actual.leadTimeByEstimate?.data,
            mapOf(*leadTimeByEstimate)
        )
    }

    fun hasThroughputByEstimate(vararg throughputByEstimate: Pair<String, Int>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.throughputByEstimate"),
            actual.throughputByEstimate?.data,
            mapOf(*throughputByEstimate)
        )
    }

    fun hasLeadTimeBySystem(vararg leadTimeBySystem: Pair<String, Double>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.leadTimeBySystem"),
            actual.leadTimeBySystem?.data,
            mapOf(*leadTimeBySystem)
        )
    }

    fun hasThroughputBySystem(vararg throughputBySystem: Pair<String, Int>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.throughputBySystem"),
            actual.throughputBySystem?.data,
            mapOf(*throughputBySystem)
        )
    }

    fun hasLeadTimeByType(vararg leadTimeByType: Pair<String, Double>) = assertAll {
        objects.assertEqual(field("issuePeriod.leadTimeByType"), actual.leadTimeByType?.data, mapOf(*leadTimeByType))
    }

    fun hasThroughputByType(vararg throughputByType: Pair<String, Int>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.throughputByType"),
            actual.throughputByType?.data,
            mapOf(*throughputByType)
        )
    }

    fun hasLeadTimeByProject(vararg leadTimeByProject: Pair<String, Double>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.leadTimeByProject"),
            actual.leadTimeByProject?.data,
            mapOf(*leadTimeByProject)
        )
    }

    fun hasThroughputByProject(vararg throughputByProject: Pair<String, Int>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.throughputByProject"),
            actual.throughputByProject?.data,
            mapOf(*throughputByProject)
        )
    }

    fun hasLeadTimeByPriority(vararg leadTimeByPriority: Pair<String, Double>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.leadTimeByPriority"),
            actual.leadTimeByPriority?.data,
            mapOf(*leadTimeByPriority)
        )
    }

    fun hasThroughputByPriority(vararg throughputByPriority: Pair<String, Int>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.throughputByPriority"),
            actual.throughputByPriority?.data,
            mapOf(*throughputByPriority)
        )
    }

    fun hasThroughput(throughput: Int) = assertAll {
        objects.assertEqual(field("issuePeriod.throughput"), actual.throughput, throughput)
    }

    fun hasWipAvg(wipAvg: Double) = assertAll {
        objects.assertEqual(field("issuePeriod.wipAvg"), actual.wipAvg, wipAvg)
    }

    fun hasAvgPctEfficiency(avgPctEfficiency: Double) = assertAll {
        objects.assertEqual(field("issuePeriod.avgPctEfficiency"), actual.avgPctEfficiency, avgPctEfficiency)
    }

    fun hasEmptyDynamicCharts() = assertAll {
        iterables.assertEmpty(field("issuePeriod.dynamicCharts"), actual.dynamicCharts)
    }

    fun hasDynamicCharts(dynamicCharts: List<DynamicChart>? = null) = assertAll {
        iterables.assertContainsExactlyInAnyOrder(
            field("issuePeriod.dynamicCharts"),
            actual.dynamicCharts,
            dynamicCharts?.toTypedArray()
        )
    }

    fun hasEmptyLeadTimeCompareChart() = assertAll {
        maps.assertEmpty(field("issuePeriod.leadTimeCompareChart"), actual.leadTimeCompareChart?.data)
    }

    fun hasLeadTimeCompareChart(leadTimeCompareChart: Map<String, Double>) = assertAll {
        objects.assertEqual(
            field("issuePeriod.leadTimeCompareChart"),
            actual.leadTimeCompareChart?.data,
            leadTimeCompareChart
        )
    }

    fun containsColumnTimeAvg(vararg args: Any?) = assertAll {
        iterables.assertContains(field("issuePeriod.columnTimeAvg"), actual.columnTimeAvg, args)
    }

}
