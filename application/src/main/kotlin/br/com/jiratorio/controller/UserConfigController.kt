package br.com.jiratorio.controller

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.domain.response.UserConfigResponse
import br.com.jiratorio.usecase.userconfig.FindUserConfigUseCase
import br.com.jiratorio.usecase.userconfig.UpdateUserConfigUseCase
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/users/me/configs")
class UserConfigController(
    private val findUserConfig: FindUserConfigUseCase,
    private val updateUserConfig: UpdateUserConfigUseCase
) {

    @GetMapping
    fun findMyConfig(@AuthenticationPrincipal account: Account): UserConfigResponse =
        findUserConfig.execute(account.username)

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateMyConfig(
        @Valid @RequestBody updateUserConfigRequest: UpdateUserConfigRequest,
        @AuthenticationPrincipal account: Account
    ): Unit =
        updateUserConfig.execute(account.username, updateUserConfigRequest)

}
