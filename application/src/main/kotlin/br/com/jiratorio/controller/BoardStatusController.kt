package br.com.jiratorio.controller

import br.com.jiratorio.service.ExternalBoardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boards/{boardId}/statuses")
internal class BoardStatusController(
    private val externalBoardService: ExternalBoardService
) {

    @GetMapping
    fun findByBoardId(@PathVariable boardId: Long): Set<String> =
        externalBoardService.findAllPossibleColumns(boardId)

}
