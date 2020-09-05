package br.com.jiratorio.usecase.userconfig

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.response.UserConfigResponse
import br.com.jiratorio.mapper.toUserConfigResponse
import br.com.jiratorio.repository.UserConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindUserConfigUseCase(
    private val userConfigRepository: UserConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(username: String): UserConfigResponse {
        log.info("Action=findUserConfig, username={}", username)

        return userConfigRepository.findByUsername(username)
            ?.toUserConfigResponse()
            ?: UserConfigResponse(username)
    }

}
