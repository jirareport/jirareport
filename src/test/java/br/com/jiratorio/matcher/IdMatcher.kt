package br.com.jiratorio.matcher

import org.hamcrest.BaseMatcher
import org.hamcrest.Description

class IdMatcher(
        private val expected: Long
) : BaseMatcher<Long>() {

    override fun matches(item: Any?): Boolean {
        return item != null && expected == item.toString().toLong()
    }

    override fun describeTo(description: Description) {
        description.appendText("value should is equal to ").appendText(expected.toString())
    }

}
