package br.com.jiratorio.testlibrary.factory.domain.request

import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.testlibrary.factory.KBacon
import net.datafaker.Faker
import org.springframework.stereotype.Component

@Component
class UpdateUserConfigFactory(
    private val faker: Faker
) : KBacon<UpdateUserConfigRequest>() {

    override fun builder(): UpdateUserConfigRequest {
        return UpdateUserConfigRequest(
            state = faker.address().state(),
            city = faker.address().city(),
            holidayToken = faker.regexify("[a-f0-9]{32}"),
            leadTimeChartType = faker.options().option(ChartType::class.java),
            throughputChartType = faker.options().option(ChartType::class.java)
        )
    }
}
