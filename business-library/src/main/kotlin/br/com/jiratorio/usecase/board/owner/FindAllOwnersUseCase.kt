package br.com.jiratorio.usecase.board.owner

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllOwnersUseCase(
    private val boardRepository: BoardRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(currentUser: String): Set<String> {
        log.info("Action=findAllOwners, currentUser={}", currentUser)

        val owners = boardRepository.findAllOwners()
        return owners + currentUser
    }

}
