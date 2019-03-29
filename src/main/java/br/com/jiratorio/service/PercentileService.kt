package br.com.jiratorio.service

import br.com.jiratorio.domain.Percentile

interface PercentileService {

    fun calculatePercentile(leadTimes: List<Long>): Percentile

}
