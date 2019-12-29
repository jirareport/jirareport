package br.com.jiratorio.service.impl

import br.com.jiratorio.service.CloneBoardService
import br.com.jiratorio.usecase.board.CloneBoard
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CloneBoardServiceImpl(
    private val cloneBoard: CloneBoard
) : CloneBoardService {

    @Transactional
    override fun clone(boardId: Long): Long =
        cloneBoard.execute(boardId)

}
