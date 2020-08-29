package br.com.jiratorio.extension

import feign.Response

fun Response.headersWithoutAuth(): Map<String, Collection<String>> =
    headers()
        .filterKeys { it != "Authorization" && it != "set-cookie" && it != "JSESSIONID" }
