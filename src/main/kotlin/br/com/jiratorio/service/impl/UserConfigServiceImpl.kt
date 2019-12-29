package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.HolidayUserConfig
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.domain.response.UserConfigResponse
import br.com.jiratorio.service.UserConfigService
import br.com.jiratorio.usecase.userconfig.FindHolidayUserConfig
import br.com.jiratorio.usecase.userconfig.FindUserConfig
import br.com.jiratorio.usecase.userconfig.UpdateUserConfig
import org.springframework.stereotype.Service

@Service
class UserConfigServiceImpl(
    val findHolidayUserConfig: FindHolidayUserConfig,
    val findUserConfig: FindUserConfig,
    val updateUserConfig: UpdateUserConfig
) : UserConfigService {

    override fun update(username: String, updateUserConfigRequest: UpdateUserConfigRequest) =
        updateUserConfig.execute(username, updateUserConfigRequest)

    override fun retrieveHolidayInfo(username: String): HolidayUserConfig =
        findHolidayUserConfig.execute(username)

    override fun findByUsername(username: String): UserConfigResponse =
        findUserConfig.execute(username)

}
