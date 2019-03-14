package br.com.jiratorio.factory.entity

import br.com.jiratorio.domain.ChartType
import br.com.jiratorio.domain.entity.UserConfig
import br.com.jiratorio.domain.Account
import br.com.jiratorio.repository.UserConfigRepository
import br.com.leonardoferreira.jbacon.JBacon
import com.github.javafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserConfigFactory(
        val userConfigRepository: UserConfigRepository,
        val faker: Faker
) : JBacon<UserConfig>() {

    override fun getDefault() =
            UserConfig().apply {
                username = usernameFromSecurity
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

    override fun getEmpty() = UserConfig()

    override fun persist(userConfig: UserConfig) {
        userConfigRepository.save(userConfig)
    }
}
