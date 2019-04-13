package br.com.jiratorio.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

val <T> T.log: Logger by object {
    private val loggers: MutableMap<Class<out Any>, Logger> = HashMap()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Logger {
        return if (thisRef == null)
            throw IllegalArgumentException("thisRef can't be null")
        else loggers.getOrPut(thisRef::class.java) {
            LoggerFactory.getLogger(thisRef::class.java)
        }
    }
}
