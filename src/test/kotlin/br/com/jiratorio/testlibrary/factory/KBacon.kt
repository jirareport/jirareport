package br.com.jiratorio.testlibrary.factory

import org.springframework.data.repository.CrudRepository
import kotlin.reflect.KFunction0
import kotlin.reflect.KProperty
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions

abstract class KBacon<T : Any>(
    private val repository: CrudRepository<T, *>? = null,
    private val shouldPersist: Boolean = repository != null
) {

    fun create(
        builder: KFunction0<T> = ::builder,
        persist: Boolean = shouldPersist,
        modifyingFields: Map<KProperty<*>, Any> = mapOf()
    ): T {
        val instance = if (modifyingFields.isEmpty())
            builder()
        else
            clone(builder(), modifyingFields)

        if (persist) {
            persist(instance)
        }

        return instance
    }

    protected open fun persist(entity: T) {
        repository?.save(entity) ?: throw UnsupportedOperationException()
    }

    fun create(
        quantity: Int,
        builder: KFunction0<T> = ::builder,
        persist: Boolean = shouldPersist,
        modifyingFields: Map<KProperty<*>, Any> = mapOf()
    ): List<T> = (1..quantity).map {
        create(builder, persist, modifyingFields)
    }

    abstract fun builder(): T

    private fun <T : Any> clone(obj: T, fields: Map<KProperty<*>, Any>): T {
        require(obj::class.isData)

        val copy = obj::class.memberFunctions.first { it.name == "copy" }
        val instanceParam = copy.instanceParameter!!

        val args = mapOf(instanceParam to obj) + fields.mapKeys { entry ->
            copy.parameters
                .first { param -> param.name == entry.key.name }
        }
        val callBy = copy.callBy(args)

        @Suppress("UNCHECKED_CAST")
        return callBy as T
    }

}
