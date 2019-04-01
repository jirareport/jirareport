package br.com.jiratorio.mapper

import br.com.jiratorio.domain.ImportHolidayInfo
import br.com.jiratorio.domain.entity.UserConfig
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.domain.response.UserConfigResponse
import br.com.jiratorio.extension.stripAccents
import org.springframework.stereotype.Component

@Component
class UserConfigMapper {

    fun userConfigToResponse(userConfig: UserConfig): UserConfigResponse {
        return UserConfigResponse(
            state = userConfig.state,
            city = userConfig.city,
            holidayToken = userConfig.holidayToken,
            leadTimeChartType = userConfig.leadTimeChartType.name,
            throughputChartType = userConfig.throughputChartType.name
        )
    }

    fun updateFromRequest(userConfig: UserConfig, updateUserConfigRequest: UpdateUserConfigRequest): UserConfig {
        return userConfig.apply {
            holidayToken = updateUserConfigRequest.holidayToken
            state = updateUserConfigRequest.state
            city = updateUserConfigRequest.city
                ?.stripAccents()
                ?.replace(" ", "")
                ?.toUpperCase()

            val leadTimeChartType = updateUserConfigRequest.leadTimeChartType
            if (leadTimeChartType != null) {
                this.leadTimeChartType = leadTimeChartType
            }

            val throughputChartType = updateUserConfigRequest.throughputChartType
            if (throughputChartType != null) {
                this.throughputChartType = throughputChartType
            }
        }
    }

    fun toImportHolidayInfo(userConfig: UserConfig): ImportHolidayInfo {
        return ImportHolidayInfo(
            state = userConfig.state!!,
            city = userConfig.city!!,
            holidayToken = userConfig.holidayToken!!
        )
    }

}
