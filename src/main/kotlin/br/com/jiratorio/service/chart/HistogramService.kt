package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Histogram

interface HistogramService {

    fun issueHistogram(issues: List<Issue>): Histogram

}
