package br.com.jiratorio.holiday.provider

import br.com.jiratorio.holiday.domain.ApiHoliday
import br.com.jiratorio.holiday.domain.HolidayEventsXml
import br.com.jiratorio.property.HolidayProperties
import tools.jackson.databind.DeserializationFeature
import tools.jackson.dataformat.xml.XmlMapper
import tools.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Base64

@Component
class HolidayResponseParser(
    private val holidayProperties: HolidayProperties,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val xmlMapper: XmlMapper = XmlMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun parse(encodedBody: String): List<ApiHoliday> {
        val xml = decode(encodedBody)
        val events = xmlMapper.readValue(xml, HolidayEventsXml::class.java)
        val filtered = events.event.filter { it.typeCode in holidayProperties.includedTypeCodes }
        if (filtered.isEmpty()) {
            log.warn("No holidays found matching included type codes {}", holidayProperties.includedTypeCodes)
        }
        return filtered.map {
            ApiHoliday(
                date = LocalDate.parse(it.date!!, dateFormatter),
                description = it.name ?: "",
            )
        }
    }

    private fun decode(body: String): String {
        val key = holidayProperties.decodeKey.toByteArray(Charsets.UTF_8)
        return try {
            val decoded = Base64.getDecoder().decode(body.trim())
            val xored = ByteArray(decoded.size) { i -> (decoded[i].toInt() xor key[i % key.size].toInt()).toByte() }
            xored.toString(Charsets.UTF_8)
        } catch (e: Exception) {
            log.error("Failed to decode holiday response body", e)
            throw IllegalStateException("holiday decode failed — key may have rotated", e)
        }
    }

}
