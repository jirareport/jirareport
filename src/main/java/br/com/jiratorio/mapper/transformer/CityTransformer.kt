package br.com.jiratorio.mapper.transformer

import org.springframework.stereotype.Component

@Component
class CityTransformer(
    private val stringTransformer: StringTransformer
) {

    fun normalizeCity(city: String?): String? {
        return if (city == null) {
            null
        } else {
            stringTransformer.stripAccents(city)
                .replace(" ", "_")
                .toUpperCase()
        }
    }

}
