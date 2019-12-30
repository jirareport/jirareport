package br.com.jiratorio.usecase.userconfig

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.UserConfig
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.mapper.updateFromUpdateUserConfigRequest
import br.com.jiratorio.repository.UserConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class UpdateUserConfig(
    private val userConfigRepository: UserConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(username: String, updateUserConfigRequest: UpdateUserConfigRequest) {
        log.info("Action=updateUserConfig, username={}, updateUserConfigRequest={}", username, updateUserConfigRequest)

        val userConfig = userConfigRepository.findByUsername(username) ?: UserConfig(username)

        userConfig.updateFromUpdateUserConfigRequest(updateUserConfigRequest)

        userConfigRepository.save(userConfig)
    }
}
