package br.com.jiratorio.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.hypersistence.utils.hibernate.type.util.ObjectMapperSupplier

class HypersistenceObjectMapperSupplier : ObjectMapperSupplier {
    override fun get(): ObjectMapper =
        ObjectMapper().findAndRegisterModules()
}
