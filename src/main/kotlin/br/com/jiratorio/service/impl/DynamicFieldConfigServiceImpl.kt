package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.domain.response.DynamicFieldConfigResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toDynamicFieldConfig
import br.com.jiratorio.mapper.toDynamicFieldConfigResponse
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.DynamicFieldConfigService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DynamicFieldConfigServiceImpl(
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository,
    private val boardService: BoardService
) : DynamicFieldConfigService {

    @Transactional(readOnly = true)
    override fun findByBoard(boardId: Long): List<DynamicFieldConfigResponse> {
        log.info("Method=findByBoard, boardId={}", boardId)

        val board = boardService.findById(boardId)
        val dynamicFields = dynamicFieldConfigRepository.findByBoard(board)

        return dynamicFields.toDynamicFieldConfigResponse()
    }

    @Transactional
    override fun create(boardId: Long, dynamicFieldConfigRequest: DynamicFieldConfigRequest): Long {
        log.info("Method=create, boardId={}, dynamicFieldConfigRequest={}", boardId, dynamicFieldConfigRequest)

        val board = boardService.findById(boardId)
        val dynamicFieldConfig = dynamicFieldConfigRequest.toDynamicFieldConfig(board)

        dynamicFieldConfigRepository.save(dynamicFieldConfig)

        return dynamicFieldConfig.id
    }

    @Transactional
    override fun deleteByBoardAndId(boardId: Long, id: Long) {
        log.info("Method=deleteByBoardAndId, boardId={}, id={}", boardId, id)

        val dynamicFieldConfig =
            dynamicFieldConfigRepository.findByBoardIdAndId(boardId, id) ?: throw ResourceNotFound()
        dynamicFieldConfigRepository.delete(dynamicFieldConfig)
    }
}
