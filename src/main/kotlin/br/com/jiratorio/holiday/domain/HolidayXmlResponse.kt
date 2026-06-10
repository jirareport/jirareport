@file:Suppress("DEPRECATION")

package br.com.jiratorio.holiday.domain

import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "events")
data class HolidayEventsXml(
    @field:JacksonXmlElementWrapper(useWrapping = false)
    @field:JacksonXmlProperty(localName = "event")
    val event: List<HolidayEventXml> = emptyList(),
)

data class HolidayEventXml(
    val date: String? = null,
    val name: String? = null,
    val description: String? = null,
    val type: String? = null,
    @field:JacksonXmlProperty(localName = "type_code")
    val typeCode: Int? = null,
    val link: String? = null,
)
