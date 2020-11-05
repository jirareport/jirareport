package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toLeadTimeConfig
import br.com.jiratorio.mapper.toLeadTimeConfigResponse
import br.com.jiratorio.mapper.updateFromLeadTimeConfigRequest
import br.com.jiratorio.repository.LeadTimeConfigRepository
import br.com.jiratorio.service.board.BoardService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LeadTimeConfigService(
    private val boardService: BoardService,
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findByBoard(boardId: Long): List<LeadTimeConfigEntity> {
        log.info("boardId={}", boardId)

        return leadTimeConfigRepository.findByBoardId(boardId)
    }

    @Transactional(readOnly = true)
    fun findAll(boardId: Long): List<LeadTimeConfigResponse> {
        log.info("boardId={}", boardId)

        return leadTimeConfigRepository.findByBoardId(boardId)
            .toLeadTimeConfigResponse()
    }

    @Transactional(readOnly = true)
    fun findByIdAndBoard(id: Long, boardId: Long): LeadTimeConfigResponse {
        log.info("id={}, boardId={}", id, boardId)

        val leadTimeConfig = leadTimeConfigRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        return leadTimeConfig.toLeadTimeConfigResponse()
    }

    @Transactional
    fun create(boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest): Long {
        log.info("boardId={}, leadTimeConfigRequest={}", boardId, leadTimeConfigRequest)

        val board = boardService.findById(boardId)
        val leadTimeConfig = leadTimeConfigRequest.toLeadTimeConfig(board)

        leadTimeConfigRepository.save(leadTimeConfig)

        return leadTimeConfig.id
    }

    @Transactional
    fun update(id: Long, boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest) {
        log.info("id={}, boardId={}, leadTimeConfigRequest={}", id, boardId, leadTimeConfigRequest)

        val leadTimeConfig = leadTimeConfigRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        leadTimeConfig.updateFromLeadTimeConfigRequest(leadTimeConfigRequest)
        leadTimeConfigRepository.save(leadTimeConfig)
    }

    @Transactional
    fun delete(id: Long, boardId: Long) {
        log.info("id={}, boardId={}", id, boardId)

        val leadTimeConfig = leadTimeConfigRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        leadTimeConfigRepository.delete(leadTimeConfig)
    }

    @Transactional
    fun clone(leadTimeConfigs: Set<LeadTimeConfigEntity>, target: BoardEntity): Unit =
        leadTimeConfigs
            .map { it.copy(id = 0, board = target) }
            .let { leadTimeConfigRepository.saveAll(it) }

}
