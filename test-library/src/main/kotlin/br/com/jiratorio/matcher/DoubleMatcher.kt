package br.com.jiratorio.matcher

import org.hamcrest.BaseMatcher
import org.hamcrest.Description

class DoubleMatcher(
    private val expected: Double
) : BaseMatcher<Double>() {

    override fun matches(item: Any?): Boolean {
        return item != null && expected == item.toString().toDouble()
    }

    override fun describeTo(description: Description) {
        description.appendText("value should is equal to ").appendText(expected.toString())
    }

}
