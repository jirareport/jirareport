package br.com.jiratorio.factory;

import br.com.jiratorio.domain.ChartType;
import br.com.jiratorio.domain.UserConfig;
import br.com.jiratorio.domain.vo.Account;
import br.com.jiratorio.repository.UserConfigRepository;
import br.com.leonardoferreira.jbacon.JBacon;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserConfigFactory extends JBacon<UserConfig> {

    @Autowired
    private UserConfigRepository userConfigRepository;

    @Autowired
    private Faker faker;

    @Override
    protected UserConfig getDefault() {
        UserConfig userConfig = new UserConfig();

        userConfig.setUsername(getUsernameFromSecurity());
        userConfig.setState(faker.address().state());
        userConfig.setCity(faker.address().city());
        userConfig.setHolidayToken(faker.crypto().md5());
        userConfig.setLeadTimeChartType(faker.options().option(ChartType.class));
        userConfig.setThroughputChartType(faker.options().option(ChartType.class));

        return userConfig;
    }

    private String getUsernameFromSecurity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return account.getUsername();
    }

    @Override
    protected UserConfig getEmpty() {
        return new UserConfig();
    }

    @Override
    protected void persist(final UserConfig userConfig) {
        userConfigRepository.save(userConfig);
    }
}
