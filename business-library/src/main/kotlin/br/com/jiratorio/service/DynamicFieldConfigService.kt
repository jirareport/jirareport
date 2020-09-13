package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toDynamicFieldConfig
import br.com.jiratorio.mapper.toDynamicFieldConfigResponse
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import br.com.jiratorio.service.board.BoardService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DynamicFieldConfigService(
    private val boardService: BoardService,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findAll(boardId: Long): List<DynamicFieldConfigResponse> {
        log.info("boardId={}", boardId)

        val board = boardService.findById(boardId)
        val dynamicFields = dynamicFieldConfigRepository.findByBoard(board)

        return dynamicFields.toDynamicFieldConfigResponse()
    }

    @Transactional
    fun create(boardId: Long, dynamicFieldConfigRequest: DynamicFieldConfigRequest): Long {
        log.info("boardId={}, dynamicFieldConfigRequest={}", boardId, dynamicFieldConfigRequest)

        val board = boardService.findById(boardId)
        val dynamicFieldConfig = dynamicFieldConfigRequest.toDynamicFieldConfig(board)

        dynamicFieldConfigRepository.save(dynamicFieldConfig)

        return dynamicFieldConfig.id
    }

    @Transactional
    fun delete(id: Long, boardId: Long) {
        log.info("id={}, boardId={}", id, boardId)

        val dynamicFieldConfig =
            dynamicFieldConfigRepository.findByBoardIdAndId(boardId, id) ?: throw ResourceNotFound()

        dynamicFieldConfigRepository.delete(dynamicFieldConfig)
    }

    @Transactional
    fun clone(dynamicFields: Set<DynamicFieldConfigEntity>, target: BoardEntity): Unit =
        dynamicFields
            .map { it.copy(id = 0, board = target) }
            .let { dynamicFieldConfigRepository.saveAll(it) }

}
