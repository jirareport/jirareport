package br.com.jiratorio.usecase.board.owner

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.Account
import br.com.jiratorio.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllOwners(
    private val boardRepository: BoardRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(currentUser: Account): Set<String> {
        log.info("Method=execute, currentUser={}", currentUser)

        val owners = boardRepository.findAllOwners()
        return owners + currentUser.username
    }

}
