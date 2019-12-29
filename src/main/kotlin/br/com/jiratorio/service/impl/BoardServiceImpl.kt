package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.usecase.board.CreateBoard
import br.com.jiratorio.usecase.board.DeleteBoard
import br.com.jiratorio.usecase.board.FindAllBoard
import br.com.jiratorio.usecase.board.FindBoard
import br.com.jiratorio.usecase.board.FindBoardDetails
import br.com.jiratorio.usecase.board.UpdateBoard
import br.com.jiratorio.usecase.board.owner.FindAllOwners
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardServiceImpl(
    private val findAllOwners: FindAllOwners,
    private val createBoard: CreateBoard,
    private val deleteBoard: DeleteBoard,
    private val findAllBoard: FindAllBoard,
    private val findBoardById: FindBoard,
    private val findBoardDetailsById: FindBoardDetails,
    private val updateBoard: UpdateBoard
) : BoardService {

    @Transactional(readOnly = true)
    override fun findAll(
        pageable: Pageable,
        searchBoardRequest: SearchBoardRequest,
        currentUser: Account
    ): Page<BoardResponse> =
        findAllBoard.execute(pageable, searchBoardRequest, currentUser)

    @Transactional
    override fun create(createBoardRequest: CreateBoardRequest): Long =
        createBoard.execute(createBoardRequest)

    @Transactional
    override fun delete(id: Long, username: String) =
        deleteBoard.execute(id, username)

    @Transactional(readOnly = true)
    override fun findById(id: Long): Board =
        findBoardById.execute(id)

    @Transactional
    override fun update(boardId: Long, updateBoardRequest: UpdateBoardRequest) =
        updateBoard.execute(boardId, updateBoardRequest)

    @Transactional(readOnly = true)
    override fun findDetailsById(id: Long): BoardDetailsResponse =
        findBoardDetailsById.execute(id)

    @Transactional(readOnly = true)
    override fun findAllOwners(currentUser: Account): Set<String> =
        findAllOwners.execute(currentUser)

}
