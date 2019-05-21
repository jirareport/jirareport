package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.Issue

interface ImpedimentService {

    fun saveImpedimentHistories(issues: List<Issue>)

}
