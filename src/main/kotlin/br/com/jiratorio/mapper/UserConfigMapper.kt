package br.com.jiratorio.mapper

import br.com.jiratorio.domain.HolidayUserConfig
import br.com.jiratorio.domain.entity.UserConfigEntity
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.domain.response.UserConfigResponse
import br.com.jiratorio.extension.stripAccents

fun UserConfigEntity.toUserConfigResponse(): UserConfigResponse =
    UserConfigResponse(
        username = username,
        state = state,
        city = city,
        holidayToken = holidayToken,
        leadTimeChartType = leadTimeChartType.name,
        throughputChartType = throughputChartType.name
    )

fun UserConfigEntity.updateFromUpdateUserConfigRequest(updateUserConfigRequest: UpdateUserConfigRequest) {
    holidayToken = updateUserConfigRequest.holidayToken
    state = updateUserConfigRequest.state
    city = updateUserConfigRequest.city
        ?.stripAccents()
        ?.replace(" ", "_")
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

fun UserConfigEntity.toImportHolidayInfo(): HolidayUserConfig =
    HolidayUserConfig(
        state = state!!,
        city = city!!,
        holidayToken = holidayToken!!
    )
