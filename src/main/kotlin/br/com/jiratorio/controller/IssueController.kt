package br.com.jiratorio.controller

import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.IssueFilterResponse
import br.com.jiratorio.domain.response.ListIssueResponse
import br.com.jiratorio.service.IssueService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/boards/{boardId}/issues")
class IssueController(private val issueService: IssueService) {

    @GetMapping
    fun index(@PathVariable boardId: Long, searchIssueRequest: SearchIssueRequest): ListIssueResponse {
        return issueService.findByExample(boardId, searchIssueRequest)
    }

    @GetMapping("/filters")
    fun filters(
        @PathVariable boardId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate
    ): IssueFilterResponse {
        return issueService.findFilters(boardId, startDate, endDate)
    }

}
