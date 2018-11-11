package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.ChartType;
import br.com.leonardoferreira.jirareport.domain.UserConfig;
import br.com.leonardoferreira.jirareport.domain.form.UserConfigForm;
import br.com.leonardoferreira.jirareport.mapper.UserConfigMapper;
import br.com.leonardoferreira.jirareport.repository.UserConfigRepository;
import br.com.leonardoferreira.jirareport.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserServiceImpl extends AbstractService implements UserService {

    @Autowired
    private UserConfigRepository userConfigRepository;

    @Autowired
    private UserConfigMapper userConfigMapper;

    @Value("${holiday.token}")
    private String holidayToken;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserConfigForm myInfo() {
        log.info("Method=myInfo");

        UserConfig userConfig = userService.retrieveCurrentUserConfig();
        return userConfigMapper.userConfigToForm(userConfig == null ? new UserConfig() : userConfig);
    }

    @Override
    @Transactional
    @CacheEvict("retrieveCurrentUserConfig")
    public void update(final UserConfigForm userConfigForm) {
        log.info("Method=update, userConfigForm={}", userConfigForm);

        UserConfig userConfig = userService.retrieveCurrentUserConfig();
        if (userConfig == null) {
            userConfig = new UserConfig();
            userConfig.setUsername(currentUser().getUsername());
        }

        userConfigMapper.updateFromForm(userConfig, userConfigForm);

        userConfigRepository.save(userConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public UserConfig findHolidayInfo() {
        log.info("Method=findHolidayInfo");

        UserConfig userConfig = userService.retrieveCurrentUserConfig();
        if (userConfig == null || StringUtils.isEmpty(userConfig.getHolidayToken())) {
            userConfig = new UserConfig();
            userConfig.setCity("ARARAQUARA");
            userConfig.setState("SP");
            userConfig.setHolidayToken(holidayToken);

            return userConfig;
        }

        return userConfig;
    }

    @Override
    @Transactional(readOnly = true)
    public ChartType retrieveFavoriteLeadTimeChartType() {
        log.info("Method=retrieveFavoriteChartType");

        UserConfig userConfig = userService.retrieveCurrentUserConfig();
        return userConfig == null ? null : userConfig.getLeadTimeChartType();
    }

    @Override
    @Transactional(readOnly = true)
    public ChartType retrieveFavoriteThroughputChartType() {
        log.info("Method=retrieveFavoriteChartType");

        UserConfig userConfig = userService.retrieveCurrentUserConfig();
        return userConfig == null ? null : userConfig.getThroughputChartType();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("retrieveCurrentUserConfig")
    public UserConfig retrieveCurrentUserConfig() {
        return userConfigRepository.findByUsername(currentUser().getUsername());
    }
}
