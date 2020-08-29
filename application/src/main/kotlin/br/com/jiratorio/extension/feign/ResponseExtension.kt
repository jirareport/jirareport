package br.com.jiratorio.extension.feign

import feign.Response

fun Response.headersWithoutAuth(): Map<String, Collection<String>> =
    this.headers().filterKeys {
        it != "Authorization" && it != "set-cookie" && it != "JSESSIONID"
    }
