package br.com.jiratorio.controller

import br.com.jiratorio.domain.external.ExternalBoard
import br.com.jiratorio.service.ExternalBoardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
internal class ProjectController(
    private val boardDataProvider: ExternalBoardService,
) {

    @GetMapping
    fun findAll(): List<ExternalBoard> =
        boardDataProvider.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ExternalBoard =
        boardDataProvider.findById(id)

}
