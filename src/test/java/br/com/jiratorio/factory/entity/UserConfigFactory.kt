package br.com.jiratorio.factory.entity

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.ChartType
import br.com.jiratorio.domain.entity.UserConfig
import br.com.jiratorio.repository.UserConfigRepository
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserConfigFactory(
    private val userConfigRepository: UserConfigRepository,
    private val faker: Faker
) : JBacon<UserConfig>() {

    override fun getDefault() =
        UserConfig(usernameFromSecurity).apply {
            state = faker.address().state()
            city = faker.address().city()
            holidayToken = faker.crypto().md5()
            leadTimeChartType = faker.options().option(ChartType::class.java)
            throughputChartType = faker.options().option(ChartType::class.java)
        }

    private val usernameFromSecurity: String
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            val account = authentication.principal as Account
            return account.username
        }

    override fun getEmpty() = UserConfig(usernameFromSecurity)

    override fun persist(userConfig: UserConfig) {
        userConfigRepository.save(userConfig)
    }
}
