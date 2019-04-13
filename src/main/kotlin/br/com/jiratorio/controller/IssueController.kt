package br.com.jiratorio.controller

import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.ListIssueResponse
import br.com.jiratorio.service.IssueService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boards/{boardId}/issues")
class IssueController(private val issueService: IssueService) {

    @GetMapping
    fun index(@PathVariable boardId: Long, searchIssueRequest: SearchIssueRequest): ListIssueResponse {
        return issueService.findByExample(boardId, searchIssueRequest)
    }

}
