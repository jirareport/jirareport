package br.com.jiratorio.extension

import kotlin.reflect.KProperty1

inline fun <reified T : Any> T.equalsBuilder(
    other: Any?,
    vararg properties: KProperty1<T, Any?>
) = when {
    this === other -> true
    this.javaClass != other?.javaClass -> false
    other !is T -> false
    else -> properties.all {
        it.get(other) == it.get(this)
    }
}
