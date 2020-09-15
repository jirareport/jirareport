package br.com.jiratorio.assertion

import br.com.jiratorio.assertion.error.ShouldBeEquals.Companion.shouldBeEquals
import br.com.jiratorio.domain.Efficiency
import org.assertj.core.api.AbstractAssert

class EfficiencyAssert(
    actual: Efficiency,
) : AbstractAssert<EfficiencyAssert, Efficiency>(
    actual,
    EfficiencyAssert::class.java
) {

    fun hasWaitTime(waitTime: Long): EfficiencyAssert {
        if (actual.waitTime != waitTime) {
            failWithMessage(shouldBeEquals(actual.waitTime, waitTime).create())
        }

        return this
    }

    fun hasTouchTime(touchTime: Long): EfficiencyAssert {
        if (actual.touchTime != touchTime) {
            failWithMessage(shouldBeEquals(actual.touchTime, touchTime).create())
        }

        return this
    }

    fun hasPctEfficiency(pctEfficiency: Double): EfficiencyAssert {
        if (actual.pctEfficiency != pctEfficiency) {
            failWithMessage(shouldBeEquals(actual.pctEfficiency, pctEfficiency).create())
        }

        return this
    }

    companion object {

        fun assertThat(actual: Efficiency): EfficiencyAssert =
            EfficiencyAssert(actual)

    }

}
