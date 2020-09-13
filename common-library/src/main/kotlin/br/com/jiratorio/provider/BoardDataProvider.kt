package br.com.jiratorio.provider

import br.com.jiratorio.domain.external.ExternalBoard

interface BoardDataProvider {

    fun findAllExternalBoards(): List<ExternalBoard>

    fun findDetails(externalId: Long): ExternalBoard
    
    fun findAllPossibleColumns(externalId: Long): Set<String>

}
