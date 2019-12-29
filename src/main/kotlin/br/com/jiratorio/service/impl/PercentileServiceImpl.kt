package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.Percentile
import br.com.jiratorio.service.PercentileService
import br.com.jiratorio.usecase.percentile.CalculatePercentile
import org.springframework.stereotype.Service

@Service
class PercentileServiceImpl(
    private val calculatePercentile: CalculatePercentile
) : PercentileService {

    override fun calculatePercentile(leadTimes: List<Long>): Percentile =
        calculatePercentile.execute(leadTimes)

}
