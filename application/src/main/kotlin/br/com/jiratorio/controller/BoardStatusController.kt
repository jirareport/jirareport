package br.com.jiratorio.controller

import br.com.jiratorio.usecase.project.FindJiraProjectStatusesUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boards/{boardId}/statuses")
class BoardStatusController(
    private val findJiraProjectStatuses: FindJiraProjectStatusesUseCase
) {

    @GetMapping
    fun findByBoardId(@PathVariable boardId: Long): Set<String> =
        findJiraProjectStatuses.execute(boardId)

}
