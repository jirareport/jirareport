package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.ChartType
import br.com.jiratorio.domain.request.UpdateUserConfigRequest
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class UpdateUserConfigFactory(
        private val faker: Faker
) : JBacon<UpdateUserConfigRequest>() {

    override fun getDefault() =
            UpdateUserConfigRequest().apply {
                state = faker.address().state()
                city = faker.address().city()
                holidayToken = faker.crypto().md5()
                leadTimeChartType = faker.options().option(ChartType::class.java)
                throughputChartType = faker.options().option(ChartType::class.java)
            }

    override fun getEmpty() =
            UpdateUserConfigRequest()

    override fun persist(updateUserConfigRequest: UpdateUserConfigRequest) =
            throw UnsupportedOperationException()

}
