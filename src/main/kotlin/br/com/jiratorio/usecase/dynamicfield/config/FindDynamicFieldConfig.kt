package br.com.jiratorio.usecase.dynamicfield.config

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse
import br.com.jiratorio.mapper.toDynamicFieldConfigResponse
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import br.com.jiratorio.usecase.board.FindBoard
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindDynamicFieldConfig(
    private val findBoardById: FindBoard,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(boardId: Long): List<DynamicFieldConfigResponse> {
        log.info("Method=execute, boardId={}", boardId)

        val board = findBoardById.execute(boardId)
        val dynamicFields = dynamicFieldConfigRepository.findByBoard(board)

        return dynamicFields.toDynamicFieldConfigResponse()
    }

}
