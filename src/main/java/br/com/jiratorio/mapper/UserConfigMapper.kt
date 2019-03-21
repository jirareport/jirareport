package br.com.jiratorio.mapper

import br.com.jiratorio.domain.ImportHolidayInfo
import br.com.jiratorio.domain.entity.UserConfig
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.domain.response.UserConfigResponse
import br.com.jiratorio.mapper.transformer.CityTransformer
import org.springframework.stereotype.Component

@Component
class UserConfigMapper(
    private val cityTransformer: CityTransformer
) {

    fun userConfigToResponse(userConfig: UserConfig) = UserConfigResponse(
        state = userConfig.state,
        city = userConfig.city,
        holidayToken = userConfig.holidayToken
    ).apply {
        leadTimeChartType = userConfig.leadTimeChartType?.name ?: leadTimeChartType
        throughputChartType = userConfig.throughputChartType?.name ?: throughputChartType
    }

    fun updateFromRequest(
        userConfig: UserConfig,
        updateUserConfigRequest: UpdateUserConfigRequest
    ) = userConfig.apply {
        state = updateUserConfigRequest.state
        city = cityTransformer.normalizeCity(updateUserConfigRequest.city)
        holidayToken = updateUserConfigRequest.holidayToken
        leadTimeChartType = updateUserConfigRequest.leadTimeChartType
        throughputChartType = updateUserConfigRequest.throughputChartType
    }

    fun toImportHolidayInfo(userConfig: UserConfig) = ImportHolidayInfo(
        state = userConfig.state!!,
        city = userConfig.city!!,
        holidayToken = userConfig.holidayToken!!
    )

}
