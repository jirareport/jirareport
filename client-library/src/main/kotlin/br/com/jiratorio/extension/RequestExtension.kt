package br.com.jiratorio.extension

import feign.Request

fun Request.headersWithoutAuth(): Map<String, Collection<String>> =
    headers()
        .filterKeys { it != "Authorization" && it != "set-cookie" && it != "JSESSIONID" }
