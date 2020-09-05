package br.com.jiratorio.usecase.dynamicfield.config

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toDynamicFieldConfig
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateDynamicFieldConfigUseCase(
    private val boardRepository: BoardRepository,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(boardId: Long, dynamicFieldConfigRequest: DynamicFieldConfigRequest): Long {
        log.info("Action=createDynamicFieldConfig, boardId={}, dynamicFieldConfigRequest={}", boardId, dynamicFieldConfigRequest)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()
        val dynamicFieldConfig = dynamicFieldConfigRequest.toDynamicFieldConfig(board)

        dynamicFieldConfigRepository.save(dynamicFieldConfig)

        return dynamicFieldConfig.id
    }

}
