package br.com.jiratorio.assertion.error

import org.assertj.core.error.BasicErrorMessageFactory
import org.assertj.core.error.ErrorMessageFactory

class ShouldBeEquals(
    actual: Any?,
    expected: Any?,
) : BasicErrorMessageFactory(
    "%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.",
    actual,
    expected
) {

    companion object {

        fun shouldBeEquals(actual: Any?, expected: Any?): ErrorMessageFactory =
            ShouldBeEquals(actual, expected)

    }

}
