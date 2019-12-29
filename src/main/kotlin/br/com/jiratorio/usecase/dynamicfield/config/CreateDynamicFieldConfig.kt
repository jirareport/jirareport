package br.com.jiratorio.usecase.dynamicfield.config

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.mapper.toDynamicFieldConfig
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import br.com.jiratorio.usecase.board.FindBoard
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateDynamicFieldConfig(
    private val findBoardById: FindBoard,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(boardId: Long, dynamicFieldConfigRequest: DynamicFieldConfigRequest): Long {
        log.info("Method=execute, boardId={}, dynamicFieldConfigRequest={}", boardId, dynamicFieldConfigRequest)

        val board = findBoardById.execute(boardId)
        val dynamicFieldConfig = dynamicFieldConfigRequest.toDynamicFieldConfig(board)

        dynamicFieldConfigRepository.save(dynamicFieldConfig)

        return dynamicFieldConfig.id
    }

}
