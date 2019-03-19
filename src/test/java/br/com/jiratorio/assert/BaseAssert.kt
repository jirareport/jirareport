package br.com.jiratorio.assert

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.WritableAssertionInfo
import org.assertj.core.internal.Iterables
import org.assertj.core.internal.Longs
import org.assertj.core.internal.Maps
import org.assertj.core.internal.Objects
import org.assertj.core.internal.Strings
import kotlin.reflect.KClass

open class BaseAssert<SELF : AbstractAssert<SELF, ACTUAL>?, ACTUAL>(actual: ACTUAL, self: KClass<*>) :
    AbstractAssert<SELF, ACTUAL>(actual, self.java) {

    companion object {
        val objects = Objects.instance()
        val iterables = Iterables.instance()
        val maps = Maps.instance()
        val strings = Strings.instance()
        val longs = Longs.instance()
    }

    fun assertThat(block: SELF.() -> Unit) =
        myself.apply(block)

    fun assertAll(function: () -> Unit) {
        try {
            function()
        } catch (assertionError: AssertionError) {
            assertionError.stackTrace = cleanStacktrace(assertionError.stackTrace)
            throw assertionError
        }
    }

    protected fun field(field: String): WritableAssertionInfo {
        val info = WritableAssertionInfo()
        info.description(field)
        return info
    }

    private fun cleanStacktrace(stackTrace: Array<StackTraceElement>): Array<StackTraceElement> {
        return stackTrace
            .filter { !isElementOfCustomAssert(it) }
            .toTypedArray()
    }

    private fun isElementOfCustomAssert(stackTraceElement: StackTraceElement): Boolean {
        var currentAssertClass: Class<*> = javaClass
        while (currentAssertClass != AbstractAssert::class.java) {
            val className = stackTraceElement.className
            if (className == currentAssertClass.name || className.startsWith("br.com.jiratorio.assert")) {
                return true
            }
            currentAssertClass = currentAssertClass.superclass
        }
        return false
    }

}
