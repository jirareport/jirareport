package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.ImportHolidayInfo;
import br.com.jiratorio.domain.entity.UserConfig;
import br.com.jiratorio.domain.form.UserConfigForm;
import br.com.jiratorio.mapper.UserConfigMapper;
import br.com.jiratorio.repository.UserConfigRepository;
import br.com.jiratorio.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserServiceImpl extends AbstractService implements UserService {

    private final UserConfigRepository userConfigRepository;

    private final UserConfigMapper userConfigMapper;

    private final String holidayToken;

    public UserServiceImpl(final UserConfigRepository userConfigRepository,
                           final UserConfigMapper userConfigMapper,
                           @Value("${holiday.token}") final String holidayToken) {
        this.userConfigRepository = userConfigRepository;
        this.userConfigMapper = userConfigMapper;
        this.holidayToken = holidayToken;
    }

    @Override
    @Transactional(readOnly = true)
    public UserConfigForm myInfo() {
        log.info("Method=myInfo");

        UserConfig userConfig = retrieveCurrentUserConfig();
        return userConfigMapper.userConfigToForm(userConfig == null ? new UserConfig() : userConfig);
    }

    @Override
    @Transactional
    @CacheEvict("retrieveCurrentUserConfig")
    public void update(final UserConfigForm userConfigForm) {
        log.info("Method=update, userConfigForm={}", userConfigForm);

        UserConfig userConfig = retrieveCurrentUserConfig();
        if (userConfig == null) {
            userConfig = new UserConfig();
            userConfig.setUsername(currentUser().getUsername());
        }

        userConfigMapper.updateFromForm(userConfig, userConfigForm);

        userConfigRepository.save(userConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public ImportHolidayInfo retrieveHolidayInfo() {
        log.info("Method=retrieveHolidayInfo");

        UserConfig userConfig = retrieveCurrentUserConfig();
        if (dontHasHolidayToken(userConfig)) {
            return ImportHolidayInfo.builder()
                    .city("ARARAQUARA")
                    .state("SP")
                    .holidayToken(holidayToken)
                    .build();
        }

        return userConfigMapper.toImportHolidayInfo(userConfig);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("retrieveCurrentUserConfig")
    public UserConfig retrieveCurrentUserConfig() {
        return userConfigRepository.findByUsername(currentUser().getUsername());
    }

    private boolean dontHasHolidayToken(final UserConfig userConfig) {
        return userConfig == null || StringUtils.isEmpty(userConfig.getHolidayToken());
    }
}
