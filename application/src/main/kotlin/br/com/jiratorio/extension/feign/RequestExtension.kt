package br.com.jiratorio.extension.feign

import feign.Request

fun Request.headersWithoutAuth(): Map<String, Collection<String>> =
    this.headers().filterKeys {
        it != "Authorization" && it != "set-cookie" && it != "JSESSIONID"
    }
