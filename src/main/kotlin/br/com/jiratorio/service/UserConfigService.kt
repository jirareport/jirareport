package br.com.jiratorio.service

import br.com.jiratorio.domain.HolidayUserConfig
import br.com.jiratorio.domain.entity.UserConfigEntity
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.domain.response.UserConfigResponse
import br.com.jiratorio.mapper.toImportHolidayInfo
import br.com.jiratorio.mapper.toUserConfigResponse
import br.com.jiratorio.mapper.updateFromUpdateUserConfigRequest
import br.com.jiratorio.property.HolidayProperties
import br.com.jiratorio.repository.UserConfigRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserConfigService(
    private val holidayProperties: HolidayProperties,
    private val userConfigRepository: UserConfigRepository,
) {

    @Transactional(readOnly = true)
    fun findByUsername(username: String): UserConfigResponse =
        userConfigRepository.findByUsername(username)
            ?.toUserConfigResponse()
            ?: UserConfigResponse(username)

    @Transactional(readOnly = true)
    fun findHolidayConfig(username: String): HolidayUserConfig =
        userConfigRepository.findByUsername(username)
            ?.toImportHolidayInfo()
            ?: HolidayUserConfig(
                state = holidayProperties.defaultState,
                city = holidayProperties.defaultCity,
                holidayToken = holidayProperties.token
            )

    @Transactional
    fun update(username: String, updateUserConfigRequest: UpdateUserConfigRequest) {
        val userConfig = userConfigRepository.findByUsername(username) ?: UserConfigEntity(username)

        userConfig.updateFromUpdateUserConfigRequest(updateUserConfigRequest)

        userConfigRepository.save(userConfig)
    }

}
