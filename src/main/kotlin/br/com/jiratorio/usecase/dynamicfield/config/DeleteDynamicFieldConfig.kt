package br.com.jiratorio.usecase.dynamicfield.config

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class DeleteDynamicFieldConfig(
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(boardId: Long, id: Long) {
        log.info("Method=execute, boardId={}, id={}", boardId, id)

        val dynamicFieldConfig =
            dynamicFieldConfigRepository.findByBoardIdAndId(boardId, id) ?: throw ResourceNotFound()
        dynamicFieldConfigRepository.delete(dynamicFieldConfig)
    }

}
