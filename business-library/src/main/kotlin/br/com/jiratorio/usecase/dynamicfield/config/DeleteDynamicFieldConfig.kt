package br.com.jiratorio.usecase.dynamicfield.config

import br.com.jiratorio.stereotype.UseCase
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
    fun execute(id: Long, boardId: Long) {
        log.info("Action=deleteDynamicFieldConfig, id={}, boardId={}", id, boardId)

        val dynamicFieldConfig =
            dynamicFieldConfigRepository.findByBoardIdAndId(boardId, id) ?: throw ResourceNotFound()

        dynamicFieldConfigRepository.delete(dynamicFieldConfig)
    }

}
