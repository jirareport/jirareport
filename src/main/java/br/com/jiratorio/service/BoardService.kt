package br.com.jiratorio.service

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BoardService {

    fun findAll(pageable: Pageable, searchBoardRequest: SearchBoardRequest, currentUser: Account): Page<BoardResponse>

    fun create(createBoardRequest: CreateBoardRequest): Long

    fun delete(id: Long, username: String)

    fun findById(id: Long): Board

    fun update(boardId: Long, updateBoardRequest: UpdateBoardRequest)

    fun findDetailsById(id: Long): BoardDetailsResponse

    fun findAllOwners(): Set<String>

}
