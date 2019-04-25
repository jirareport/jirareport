package br.com.jiratorio.factory

import org.springframework.data.repository.CrudRepository
import kotlin.reflect.KFunction0

abstract class KBacon<T>(
    val repository: CrudRepository<T, *>? = null
) {

    fun create(
        builder: KFunction0<T> = ::builder,
        persist: Boolean = repository != null,
        modifier: (T) -> Unit = {}
    ): T {
        return builder().also {
            modifier(it)
            if (persist) {
                persist(it)
            }
        }
    }

    private fun persist(entity: T) {
        repository?.save(entity) ?: throw UnsupportedOperationException()
    }

    fun create(
        quantity: Int,
        builder: KFunction0<T> = ::builder,
        persist: Boolean = repository != null,
        modifier: T.() -> Unit = {}
    ): List<T> = (1..quantity).map {
        create(builder, persist, modifier)
    }

    abstract fun builder(): T

}
