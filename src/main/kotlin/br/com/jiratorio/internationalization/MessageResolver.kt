package br.com.jiratorio.internationalization

import java.util.Locale

interface MessageResolver {

    val locale: Locale

    fun resolve(key: String, vararg args: Any?): String

}
