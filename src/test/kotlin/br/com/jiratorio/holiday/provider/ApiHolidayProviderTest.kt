package br.com.jiratorio.holiday.provider

import br.com.jiratorio.holiday.client.HolidayClient
import br.com.jiratorio.property.HolidayProperties
import br.com.jiratorio.testlibrary.junit.testtype.UnitTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.Base64

@UnitTest
class ApiHolidayProviderTest {

    private val key = "AFDsa%1!!2341R%#!\$\$"

    private fun encode(xml: String): String {
        val keyBytes = key.toByteArray(Charsets.UTF_8)
        val data = xml.toByteArray(Charsets.UTF_8)
        val xored = ByteArray(data.size) { i -> (data[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte() }
        return Base64.getEncoder().encodeToString(xored)
    }

    private fun buildParser(includedTypeCodes: Set<Int> = setOf(1, 2, 3)): HolidayResponseParser {
        val properties = mockk<HolidayProperties> {
            every { decodeKey } returns key
            every { this@mockk.includedTypeCodes } returns includedTypeCodes
        }
        return HolidayResponseParser(properties)
    }

    @Nested
    inner class ParserTests {

        private val parser = buildParser()

        @Test
        fun `decode and parse single real event`() {
            val xml = """<?xml version="1.0" encoding="UTF-8"?><events><event><date>01/01/2019</date><name>Ano Novo</name><type_code>1</type_code></event></events>"""
            val result = parser.parse(encode(xml))
            assertThat(result).hasSize(1)
            assertThat(result[0].date).isEqualTo(LocalDate.of(2019, 1, 1))
            assertThat(result[0].description).isEqualTo("Ano Novo")
        }

        @Test
        fun `filter excludes type codes 4 and 9`() {
            val xml = """<?xml version="1.0" encoding="UTF-8"?>
<events>
  <event><date>01/01/2019</date><name>Nacional</name><type_code>1</type_code></event>
  <event><date>01/02/2019</date><name>Estadual</name><type_code>2</type_code></event>
  <event><date>01/03/2019</date><name>Municipal</name><type_code>3</type_code></event>
  <event><date>01/04/2019</date><name>Facultativo</name><type_code>4</type_code></event>
  <event><date>01/05/2019</date><name>Convencional</name><type_code>9</type_code></event>
</events>"""
            val result = parser.parse(encode(xml))
            assertThat(result).hasSize(3)
            assertThat(result.map { it.date }).containsExactly(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 2, 1),
                LocalDate.of(2019, 3, 1),
            )
            assertThat(result.map { it.description }).containsExactly("Nacional", "Estadual", "Municipal")
        }

        @Test
        fun `decode failure throws with descriptive message`() {
            assertThatThrownBy { parser.parse("not-base64!!") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("key may have rotated")
        }

        @Test
        fun `empty events returns empty list without throwing`() {
            val xml = """<?xml version="1.0" encoding="UTF-8"?><events></events>"""
            val result = parser.parse(encode(xml))
            assertThat(result).isEmpty()
        }
    }

    @Nested
    inner class CityNormalizationTests {

        private val holidayClient = mockk<HolidayClient>()
        private val parser = mockk<HolidayResponseParser> {
            every { parse(any()) } returns emptyList()
        }
        private val provider = ApiHolidayProvider(holidayClient, parser)

        @Test
        fun `normalize accented city before calling client`() {
            val citySlot = slot<String>()
            every { holidayClient.findAllHolidaysInCity(any(), any(), capture(citySlot)) } returns ""

            provider.findAllHolidays(2026, "SP", "São Paulo")

            assertThat(citySlot.captured).isEqualTo("SAO PAULO")
        }

        @Test
        fun `normalize city strips apostrophe`() {
            val citySlot = slot<String>()
            every { holidayClient.findAllHolidaysInCity(any(), any(), capture(citySlot)) } returns ""

            provider.findAllHolidays(2026, "SP", "D’Oeste")

            assertThat(citySlot.captured).isEqualTo("DOESTE")
        }
    }
}
