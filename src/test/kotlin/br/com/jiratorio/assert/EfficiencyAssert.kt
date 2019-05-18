package br.com.jiratorio.assert

import br.com.jiratorio.domain.Efficiency

class EfficiencyAssert(actual: Efficiency) : BaseAssert<EfficiencyAssert, Efficiency>(actual, EfficiencyAssert::class) {

    fun hasWaitTime(waitTime: Long) = assertAll {
        objects.assertEqual(field("efficiency.waitTime"), actual.waitTime, waitTime)
    }

    fun hasTouchTime(touchTime: Long) = assertAll {
        objects.assertEqual(field("efficiency.touchTime"), actual.touchTime, touchTime)
    }

    fun hasPctEfficiency(pctEfficiency: Double) = assertAll {
        objects.assertEqual(field("efficiency.pctEfficiency"), actual.pctEfficiency, pctEfficiency)
    }

}
