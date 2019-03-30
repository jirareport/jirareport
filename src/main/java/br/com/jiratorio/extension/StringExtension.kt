package br.com.jiratorio.extension

import java.util.Base64

fun String.toBase64(): String =
    String(Base64.getEncoder().encode(this.toByteArray()))
