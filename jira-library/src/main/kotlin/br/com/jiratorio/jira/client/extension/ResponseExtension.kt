package br.com.jiratorio.jira.client.extension

import feign.Response

fun Response.headersWithoutAuth(): Map<String, Collection<String>> =
    headers()
        .filterKeys { it != "Authorization" && it != "set-cookie" && it != "JSESSIONID" }
