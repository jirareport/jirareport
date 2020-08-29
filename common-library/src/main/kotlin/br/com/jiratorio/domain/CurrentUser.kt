package br.com.jiratorio.domain

interface CurrentUser {

    val username: String
    
    val jiraToken: String

}
