package br.com.jiratorio.controller

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.HolidayRequest
import br.com.jiratorio.domain.response.holiday.HolidayResponse
import br.com.jiratorio.usecase.holiday.CreateHoliday
import br.com.jiratorio.usecase.holiday.DeleteHoliday
import br.com.jiratorio.usecase.holiday.FindAllHolidays
import br.com.jiratorio.usecase.holiday.FindHoliday
import br.com.jiratorio.usecase.holiday.ImportHolidays
import br.com.jiratorio.usecase.holiday.UpdateHoliday
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
    private val createHoliday: CreateHoliday,
    private val deleteHoliday: DeleteHoliday,
    private val findHoliday: FindHoliday,
    private val findAllHolidays: FindAllHolidays,
    private val importHolidays: ImportHolidays,
    private val updateHoliday: UpdateHoliday
) {

    @GetMapping
    fun index(
        @PathVariable boardId: Long,
        @PageableDefault(sort = ["date"]) pageable: Pageable
    ): Page<HolidayResponse> =
        findAllHolidays.execute(boardId, pageable)

    @PostMapping
    fun create(
        @PathVariable boardId: Long,
        @Valid @RequestBody holidayRequest: HolidayRequest
    ): HttpEntity<*> {
        val id = createHoliday.execute(boardId, holidayRequest)

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
    ) =
        importHolidays.execute(boardId, account.username)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) =
        deleteHoliday.execute(id)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): HolidayResponse =
        findHoliday.execute(id)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{holidayId}")
    fun update(
        @PathVariable boardId: Long,
        @PathVariable holidayId: Long,
        @Valid @RequestBody holidayRequest: HolidayRequest
    ) =
        updateHoliday.execute(holidayId, boardId, holidayRequest)

}
