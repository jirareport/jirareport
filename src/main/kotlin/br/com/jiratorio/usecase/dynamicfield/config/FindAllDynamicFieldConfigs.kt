package br.com.jiratorio.usecase.dynamicfield.config

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toDynamicFieldConfigResponse
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllDynamicFieldConfigs(
    private val boardRepository: BoardRepository,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(boardId: Long): List<DynamicFieldConfigResponse> {
        log.info("Action=findAllDynamicFieldConfigs, boardId={}", boardId)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()
        val dynamicFields = dynamicFieldConfigRepository.findByBoard(board)

        return dynamicFields.toDynamicFieldConfigResponse()
    }

}
