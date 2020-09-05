package br.com.jiratorio.controller

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.usecase.board.CloneBoardUseCase
import br.com.jiratorio.usecase.board.CreateBoardUseCase
import br.com.jiratorio.usecase.board.DeleteBoardUseCase
import br.com.jiratorio.usecase.board.FindAllBoardsUseCase
import br.com.jiratorio.usecase.board.FindBoardUseCase
import br.com.jiratorio.usecase.board.UpdateBoardUseCase
import br.com.jiratorio.usecase.board.owner.FindAllOwnersUseCase
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
    private val findAllOwners: FindAllOwnersUseCase,
    private val createBoard: CreateBoardUseCase,
    private val deleteBoard: DeleteBoardUseCase,
    private val findAllBoards: FindAllBoardsUseCase,
    private val findBoard: FindBoardUseCase,
    private val updateBoard: UpdateBoardUseCase,
    private val cloneBoard: CloneBoardUseCase
) {

    @GetMapping
    fun index(
        searchBoardRequest: SearchBoardRequest,
        @PageableDefault(size = 20, sort = ["id"]) pageable: Pageable,
        @AuthenticationPrincipal currentUser: Account
    ): Page<BoardResponse> =
        findAllBoards.execute(searchBoardRequest, currentUser.username, pageable)

    @GetMapping("/owners")
    fun owners(@AuthenticationPrincipal currentUser: Account): Set<String> =
        findAllOwners.execute(currentUser.username)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): BoardDetailsResponse =
        findBoard.execute(id)

    @PostMapping
    fun create(@Valid @RequestBody board: CreateBoardRequest): HttpEntity<*> {
        val id = createBoard.execute(board)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(id)

        return ResponseEntity.created(location).build<Any>()
    }

    @PostMapping(params = ["boardIdToClone"])
    fun clone(@RequestParam("boardIdToClone") boardId: Long): HttpEntity<*> {
        val id: Long = cloneBoard.execute(boardId)

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
        deleteBoard.execute(id, currentUser.username)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody updateBoardRequest: UpdateBoardRequest
    ): Unit =
        updateBoard.execute(id, updateBoardRequest)

}
