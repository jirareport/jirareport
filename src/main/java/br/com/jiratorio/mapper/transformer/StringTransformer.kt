package br.com.jiratorio.mapper.transformer

import org.springframework.stereotype.Component
import java.text.Normalizer

@Component
class StringTransformer {

    fun toUpperCase(str: String?): String? {
        return str?.toUpperCase()
    }

    fun listToUpperCase(strs: MutableList<String>?): MutableList<String>? {
        return strs?.map { it.toUpperCase() }?.toMutableList()
    }

    fun stripAccents(s: String): String {
        var str = Normalizer.normalize(s, Normalizer.Form.NFD)
        str = str.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        return str
    }

}
