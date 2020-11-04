package br.com.jiratorio.controller

import br.com.jiratorio.domain.response.FieldResponse
import br.com.jiratorio.service.FieldService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fields")
internal class FieldController(
    private val fieldService: FieldService,
) {

    @GetMapping
    fun fields(): List<FieldResponse> =
        fieldService.findAll()

}
