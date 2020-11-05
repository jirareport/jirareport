package br.com.jiratorio.provider

import br.com.jiratorio.domain.response.FieldResponse

interface FieldProvider {

    fun findAll(): List<FieldResponse>

}
