package br.com.jiratorio.service

import br.com.jiratorio.domain.response.FieldResponse
import br.com.jiratorio.provider.FieldProvider
import org.springframework.stereotype.Service

@Service
class FieldService(
    private val fieldProvider: FieldProvider,
) {

    fun findAll(): List<FieldResponse> =
        fieldProvider.findAll()

}
