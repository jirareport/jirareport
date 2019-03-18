package br.com.jiratorio.assert

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

    fun hasAvgLeadTime(avgLeadTime: Double) = assertAll {
        objects.assertEqual(field("issuePeriod.avgLeadTime"), actual.avgLeadTime, avgLeadTime)
    }

    fun histogram() =
            HistogramAssert(actual.histogram)

    fun hasLeadTimeBySize(vararg leadTimeBySize: Pair<String, Double>) = assertAll {
        objects.assertEqual(field("issuePeriod.leadTimeBySize"), actual.leadTimeBySize.data, mapOf(*leadTimeBySize))
    }

    fun hasEstimated(vararg estimated: Pair<String, Long>) = assertAll {
        objects.assertEqual(field("issuePeriod.Estimated"), actual.estimated.data, mapOf(*estimated))
    }

    fun hasLeadTimeBySystem(vararg leadTimeBySystem: Pair<String, Double>) = assertAll {
        objects.assertEqual(field("issuePeriod.leadTimeBySystem"), actual.leadTimeBySystem.data, mapOf(*leadTimeBySystem))
    }

    fun hasTasksBySystem(vararg tasksBySystem: Pair<String, Long>) = assertAll {
        objects.assertEqual(field("issuePeriod.tasksBySystem"), actual.tasksBySystem.data, mapOf(*tasksBySystem))
    }

    fun hasLeadTimeByType(vararg leadTimeByType: Pair<String, Double>) = assertAll {
        objects.assertEqual(field("issuePeriod.leadTimeByType"), actual.leadTimeByType.data, mapOf(*leadTimeByType))
    }

    fun hasTasksByType(vararg tasksByType: Pair<String, Long>) = assertAll {
        objects.assertEqual(field("issuePeriod.tasksByType"), actual.tasksByType.data, mapOf(*tasksByType))
    }

    fun hasLeadTimeByProject(vararg leadTimeByProject: Pair<String, Double>) = assertAll {
        objects.assertEqual(field("issuePeriod.leadTimeByProject"), actual.leadTimeByProject.data, mapOf(*leadTimeByProject))
    }

    fun hasTasksByProject(vararg tasksByProject: Pair<String, Long>) = assertAll {
        objects.assertEqual(field("issuePeriod.tasksByProject"), actual.tasksByProject.data, mapOf(*tasksByProject))
    }

    fun hasLeadTimeByPriority(vararg leadTimeByPriority: Pair<String, Double>) = assertAll {
        objects.assertEqual(field("issuePeriod.leadTimeByPriority"), actual.leadTimeByPriority.data, mapOf(*leadTimeByPriority))
    }

    fun hasThroughputByPriority(vararg throughputByPriority: Pair<String, Long>) = assertAll {
        objects.assertEqual(field("issuePeriod.throughputByPriority"), actual.throughputByPriority.data, mapOf(*throughputByPriority))
    }

    fun hasIssuesCount(issuesCount: Int) = assertAll {
        objects.assertEqual(field("issuePeriod.issuesCount"), actual.issuesCount, issuesCount)
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

    fun hasEmptyLeadTimeCompareChart() = assertAll {
        maps.assertEmpty(field("issuePeriod.leadTimeCompareChart"), actual.leadTimeCompareChart.data)
    }

    fun containsColumnTimeAvgs(vararg args: Any?) = assertAll {
        iterables.assertContains(field("issuePeriod.columnTimeAvgs"), actual.columnTimeAvgs, args)
    }

}
