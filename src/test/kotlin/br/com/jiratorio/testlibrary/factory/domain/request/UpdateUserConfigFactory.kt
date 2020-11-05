package br.com.jiratorio.testlibrary.factory.domain.request

import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.jiratorio.testlibrary.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class UpdateUserConfigFactory(
    private val faker: Faker
) : KBacon<UpdateUserConfigRequest>() {

    override fun builder(): UpdateUserConfigRequest {
        return UpdateUserConfigRequest(
            state = faker.address().state(),
            city = faker.address().city(),
            holidayToken = faker.crypto().md5(),
            leadTimeChartType = faker.options().option(ChartType::class.java),
            throughputChartType = faker.options().option(ChartType::class.java)
        )
    }
}
