package br.com.jiratorio.testlibrary.factory.domain.entity

import br.com.jiratorio.domain.chart.ChartType
import br.com.jiratorio.domain.entity.UserConfigEntity
import br.com.jiratorio.extension.account
import br.com.jiratorio.testlibrary.factory.KBacon
import br.com.jiratorio.repository.UserConfigRepository
import net.datafaker.Faker
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserConfigFactory(
    private val faker: Faker,
    userConfigRepository: UserConfigRepository?
) : KBacon<UserConfigEntity>(userConfigRepository) {

    override fun builder(): UserConfigEntity {
        return UserConfigEntity(
            username = SecurityContextHolder.getContext().account!!.username,
            state = faker.address().state(),
            city = faker.address().city(),
            holidayToken = faker.regexify("[a-f0-9]{32}"),
            leadTimeChartType = faker.options().option(ChartType::class.java),
            throughputChartType = faker.options().option(ChartType::class.java)
        )
    }

}
