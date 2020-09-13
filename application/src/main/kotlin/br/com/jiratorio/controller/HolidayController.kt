package br.com.jiratorio.controller

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import br.com.jiratorio.service.HolidayService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/boards/{boardId}/holidays")
class HolidayController(
    private val holidayService: HolidayService
) {
    
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun index(
        @PathVariable boardId: Long,
        @PageableDefault(sort = ["date"]) pageable: Pageable
    ): Page<HolidayResponse> =
        holidayService.findAll(boardId, pageable)

    @PostMapping
    fun create(
        @PathVariable boardId: Long,
        @Valid @RequestBody holidayRequest: HolidayRequest
    ): HttpEntity<*> {
        val id = holidayService.create(boardId, holidayRequest)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(id)

        return ResponseEntity.created(location).build<Any>()
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(params = ["import=true"])
    fun importHolidays(
        @PathVariable boardId: Long,
        @AuthenticationPrincipal account: Account
    ): Nothing {
        log.info("boardId=$boardId, account=$account")
        throw UnsupportedOperationException("temporary unavailable")
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{holidayId}")
    fun delete(@PathVariable boardId: Long, @PathVariable holidayId: Long) =
        holidayService.delete(boardId, holidayId)

    @GetMapping("/{holidayId}")
    fun findById(@PathVariable boardId: Long, @PathVariable holidayId: Long): HolidayResponse =
        holidayService.findById(boardId, holidayId)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{holidayId}")
    fun update(
        @PathVariable boardId: Long,
        @PathVariable holidayId: Long,
        @Valid @RequestBody holidayRequest: HolidayRequest
    ) =
        holidayService.update(boardId, holidayId, holidayRequest)

}
