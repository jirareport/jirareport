package br.com.jiratorio.extension

import kotlin.reflect.KProperty1

inline fun <reified T : Any> T.toStringBuilder(vararg properties: KProperty1<T, Any?>) =
    properties.joinToString(prefix = "${T::class.simpleName}(", postfix = ")") {
        "${it.name}=${it.get(this)}"
    }
