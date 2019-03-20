package br.com.jiratorio.controller

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.HolidayResponse
import br.com.jiratorio.service.HolidayService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/boards/{boardId}/holidays")
class HolidayController(private val holidayService: HolidayService) {

    @GetMapping
    fun index(
        @PathVariable boardId: Long,
        @PageableDefault(sort = ["date"]) pageable: Pageable
    ): Page<HolidayResponse> {
        return holidayService.findByBoard(boardId, pageable)
    }

    @PostMapping
    fun create(
        @PathVariable boardId: Long,
        @Valid @RequestBody holiday: HolidayRequest
    ): ResponseEntity<*> {
        val id = holidayService.create(boardId, holiday)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(id)

        return ResponseEntity.created(location).build<Any>()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<*> {
        holidayService.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }

    @PostMapping("/import")
    fun importFromAPI(
        @PathVariable boardId: Long,
        @AuthenticationPrincipal account: Account
    ): ResponseEntity<*> {
        holidayService.createImported(boardId, account)
        return ResponseEntity<Any>(HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): HolidayResponse {
        return holidayService.findById(id)
    }

    @PutMapping("/{holidayId}")
    fun update(
        @PathVariable boardId: Long,
        @PathVariable holidayId: Long,
        @Valid @RequestBody holidayRequest: HolidayRequest
    ): ResponseEntity<*> {
        holidayService.update(boardId, holidayId, holidayRequest)
        return ResponseEntity.noContent().build<Any>()
    }

}
