package br.com.jiratorio.controller

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.service.BoardService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    @GetMapping
    fun index(
        pageable: Pageable,
        searchBoardRequest: SearchBoardRequest,
        @AuthenticationPrincipal currentUser: Account
    ): Page<BoardResponse> {
        return boardService.findAll(pageable, searchBoardRequest, currentUser)
    }

    @GetMapping("/owners")
    fun owners(): Set<String> {
        return boardService.findAllOwners()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): BoardDetailsResponse {
        return boardService.findDetailsById(id)
    }

    @PostMapping
    fun create(@Valid @RequestBody board: CreateBoardRequest): ResponseEntity<*> {
        val boardId = boardService.create(board)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(boardId)

        return ResponseEntity.created(location).build<Any>()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long, @AuthenticationPrincipal currentUser: Account): ResponseEntity<*> {
        boardService.delete(id, currentUser.username)
        return ResponseEntity.noContent().build<Any>()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody updateBoardRequest: UpdateBoardRequest): ResponseEntity<*> {
        boardService.update(id, updateBoardRequest)
        return ResponseEntity.noContent().build<Any>()
    }

}
