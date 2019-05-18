package br.com.jiratorio.controller

import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import br.com.jiratorio.service.leadtime.LeadTimeConfigService
import org.springframework.http.ResponseEntity
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
@RequestMapping("/boards/{boardId}/lead-time-configs")
class LeadTimeConfigController(
    private val leadTimeConfigService: LeadTimeConfigService
) {

    @GetMapping
    fun index(@PathVariable boardId: Long): List<LeadTimeConfigResponse> {
        return leadTimeConfigService.findAll(boardId)
    }

    @PostMapping
    fun create(
        @PathVariable boardId: Long,
        @Valid @RequestBody leadTimeConfigRequest: LeadTimeConfigRequest
    ): ResponseEntity<Any> {
        val id = leadTimeConfigService.create(boardId, leadTimeConfigRequest)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(id)

        return ResponseEntity.created(location).build()
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable boardId: Long,
        @PathVariable id: Long
    ): LeadTimeConfigResponse {
        return leadTimeConfigService.findByBoardAndId(boardId, id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable boardId: Long,
        @PathVariable id: Long,
        @Valid @RequestBody leadTimeConfigRequest: LeadTimeConfigRequest
    ): ResponseEntity<*> {
        leadTimeConfigService.update(boardId, id, leadTimeConfigRequest)
        return ResponseEntity.noContent().build<Any>()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable boardId: Long,
        @PathVariable id: Long
    ): ResponseEntity<*> {
        leadTimeConfigService.deleteByBoardAndId(boardId, id)
        return ResponseEntity.noContent().build<Any>()
    }
}
