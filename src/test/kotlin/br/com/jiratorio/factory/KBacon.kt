package br.com.jiratorio.factory

import kotlin.reflect.KFunction0

abstract class KBacon<T> {

    val default: T
        get() = builder()

    fun create(
        builder: KFunction0<T> = ::builder,
        modifier: (T) -> Unit = {}
    ): T {
        return builder().also {
            modifier(it)
            persist(it)
        }
    }

    fun create(
        quantity: Int,
        builder: KFunction0<T> = ::builder,
        modifier: (T) -> Unit = {}
    ): List<T> {
        return (1..quantity).map { create(builder, modifier) }
    }

    protected abstract fun builder(): T

    protected abstract fun persist(entity: T)

}
