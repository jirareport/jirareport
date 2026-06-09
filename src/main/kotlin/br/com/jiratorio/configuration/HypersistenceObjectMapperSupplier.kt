package br.com.jiratorio.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.hypersistence.utils.hibernate.type.util.ObjectMapperSupplier

/**
 * hypersistence-utils uses Jackson 2 internally for JSON columns. Spring Boot 4's primary
 * Jackson is version 3, so hypersistence falls back to a bare Jackson 2 ObjectMapper without
 * the JavaTime / Kotlin modules. Register them here so JSON columns containing java.time types
 * and Kotlin data classes (de)serialize correctly.
 */
class HypersistenceObjectMapperSupplier : ObjectMapperSupplier {
    override fun get(): ObjectMapper =
        ObjectMapper().findAndRegisterModules()
}
