package br.com.jiratorio.controller

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.domain.response.UserConfigResponse
import br.com.jiratorio.service.UserConfigService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/users/me/configs")
class UserConfigController(private val userConfigService: UserConfigService) {

    @GetMapping
    fun findMyConfig(@AuthenticationPrincipal account: Account): UserConfigResponse {
        return userConfigService.findByUsername(account.username)
    }

    @PutMapping
    fun updateMyConfig(
        @Valid @RequestBody updateUserConfigRequest: UpdateUserConfigRequest,
        @AuthenticationPrincipal account: Account
    ): ResponseEntity<*> {
        userConfigService.update(account.username, updateUserConfigRequest)
        return ResponseEntity.noContent().build<Any>()
    }

}
