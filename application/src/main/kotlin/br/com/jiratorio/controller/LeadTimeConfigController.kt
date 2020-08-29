package br.com.jiratorio.controller

import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import br.com.jiratorio.usecase.leadtime.config.CreateLeadTimeConfig
import br.com.jiratorio.usecase.leadtime.config.DeleteLeadTimeConfig
import br.com.jiratorio.usecase.leadtime.config.FindAllLeadTimeConfigs
import br.com.jiratorio.usecase.leadtime.config.FindLeadTimeConfig
import br.com.jiratorio.usecase.leadtime.config.UpdateLeadTimeConfig
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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/boards/{boardId}/lead-time-configs")
class LeadTimeConfigController(
    private val createLeadTimeConfig: CreateLeadTimeConfig,
    private val deleteLeadTimeConfig: DeleteLeadTimeConfig,
    private val findAllLeadTimeConfigs: FindAllLeadTimeConfigs,
    private val findLeadTimeConfig: FindLeadTimeConfig,
    private val updateLeadTimeConfig: UpdateLeadTimeConfig
) {

    @GetMapping
    fun index(@PathVariable boardId: Long): List<LeadTimeConfigResponse> =
        findAllLeadTimeConfigs.execute(boardId)

    @PostMapping
    fun create(
        @PathVariable boardId: Long,
        @Valid @RequestBody leadTimeConfigRequest: LeadTimeConfigRequest
    ): HttpEntity<*> {
        val id = createLeadTimeConfig.execute(boardId, leadTimeConfigRequest)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(id)

        return ResponseEntity.created(location).build<Any>()
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable boardId: Long,
        @PathVariable id: Long
    ): LeadTimeConfigResponse =
        findLeadTimeConfig.execute(id, boardId)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable boardId: Long,
        @PathVariable id: Long,
        @Valid @RequestBody leadTimeConfigRequest: LeadTimeConfigRequest
    ): Unit =
        updateLeadTimeConfig.execute(id, boardId, leadTimeConfigRequest)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable boardId: Long,
        @PathVariable id: Long
    ): Unit =
        deleteLeadTimeConfig.execute(id, boardId)

}
