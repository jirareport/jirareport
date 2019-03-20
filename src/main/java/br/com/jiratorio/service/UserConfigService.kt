package br.com.jiratorio.service

import br.com.jiratorio.domain.ImportHolidayInfo
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.domain.response.UserConfigResponse

interface UserConfigService {

    fun update(username: String, updateUserConfigRequest: UpdateUserConfigRequest)

    fun retrieveHolidayInfo(username: String): ImportHolidayInfo

    fun findByUsername(username: String): UserConfigResponse

}
