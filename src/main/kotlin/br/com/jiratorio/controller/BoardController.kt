package br.com.jiratorio.controller

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.CloneBoardService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/boards")
class BoardController(
    private val boardService: BoardService,
    private val cloneBoardService: CloneBoardService
) {

    @GetMapping
    fun index(
        searchBoardRequest: SearchBoardRequest,
        @PageableDefault(size = 20, sort = ["id"]) pageable: Pageable,
        @AuthenticationPrincipal currentUser: Account
    ): Page<BoardResponse> =
        boardService.findAll(pageable, searchBoardRequest, currentUser)

    @GetMapping("/owners")
    fun owners(@AuthenticationPrincipal currentUser: Account): Set<String> =
        boardService.findAllOwners(currentUser)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): BoardDetailsResponse =
        boardService.findDetailsById(id)

    @PostMapping
    fun create(@Valid @RequestBody board: CreateBoardRequest): HttpEntity<Any> {
        val id = boardService.create(board)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(id)

        return ResponseEntity.created(location).build<Any>()
    }

    @PostMapping(params = ["boardIdToClone"])
    fun clone(@RequestParam("boardIdToClone") boardId: Long): HttpEntity<Any> {
        val id: Long = cloneBoardService.clone(boardId)

        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/boards/{id}")
            .build(id)

        return ResponseEntity.created(location).build<Any>()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable id: Long,
        @AuthenticationPrincipal currentUser: Account
    ): Unit =
        boardService.delete(id, currentUser.username)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody updateBoardRequest: UpdateBoardRequest
    ): Unit =
        boardService.update(id, updateBoardRequest)

}
