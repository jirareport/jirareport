package br.com.jiratorio.usecase.userconfig

import br.com.jiratorio.config.property.HolidayProperties
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.HolidayUserConfig
import br.com.jiratorio.mapper.toImportHolidayInfo
import br.com.jiratorio.repository.UserConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindHolidayUserConfig(
    private val userConfigRepository: UserConfigRepository,
    private val holidayProperties: HolidayProperties
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(username: String): HolidayUserConfig {
        log.info("Action=findHolidayUserConfig, username={}", username)

        val userConfig = userConfigRepository.findByUsername(username)

        return if (userConfig == null || userConfig.holidayToken.isNullOrEmpty())
            HolidayUserConfig(
                state = holidayProperties.defaultState,
                city = holidayProperties.defaultCity,
                holidayToken = holidayProperties.token
            )
        else
            userConfig.toImportHolidayInfo()
    }

}
