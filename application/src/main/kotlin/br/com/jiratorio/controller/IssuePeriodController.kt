package br.com.jiratorio.controller

import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByIdResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodListResponse
import br.com.jiratorio.usecase.issue.period.CreateIssuePeriodUseCase
import br.com.jiratorio.usecase.issue.period.DeleteIssuePeriodUseCase
import br.com.jiratorio.usecase.issue.period.FindAllIssuePeriodsUseCase
import br.com.jiratorio.usecase.issue.period.FindIssuePeriodUseCase
import br.com.jiratorio.usecase.issue.period.UpdateIssuePeriodUseCase
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
import java.time.LocalDate
import javax.validation.Valid

@RestController
@RequestMapping("/boards/{boardId}/issue-periods")
class IssuePeriodController(
    private val createIssuePeriod: CreateIssuePeriodUseCase,
    private val deleteIssuePeriod: DeleteIssuePeriodUseCase,
    private val findAllIssuePeriods: FindAllIssuePeriodsUseCase,
    private val findIssuePeriod: FindIssuePeriodUseCase,
    private val updateIssuePeriod: UpdateIssuePeriodUseCase,
) {

    @GetMapping
    fun index(
        @PathVariable boardId: Long,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate,
    ): IssuePeriodListResponse =
        findAllIssuePeriods.execute(FindAllIssuePeriodsFilter(boardId, startDate, endDate))

    @GetMapping("/{issuePeriodId}")
    fun findById(
        @PathVariable boardId: Long,
        @PathVariable issuePeriodId: Long,
    ): IssuePeriodByIdResponse =
        findIssuePeriod.execute(issuePeriodId, boardId)

    @PostMapping
    fun create(
        @PathVariable boardId: Long,
        @Valid @RequestBody createIssuePeriodRequest: CreateIssuePeriodRequest,
    ): HttpEntity<*> {
        val id = createIssuePeriod.execute(createIssuePeriodRequest, boardId)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(id)

        return ResponseEntity.created(location).build<Any>()
    }

    @PutMapping("/{issuePeriodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable boardId: Long,
        @PathVariable issuePeriodId: Long,
    ): Unit =
        updateIssuePeriod.execute(issuePeriodId, boardId)

    @DeleteMapping("/{issuePeriodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(
        @PathVariable boardId: Long,
        @PathVariable issuePeriodId: Long,
    ): Unit =
        deleteIssuePeriod.execute(issuePeriodId, boardId)

}
