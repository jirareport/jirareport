package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.UserConfig;
import br.com.leonardoferreira.jirareport.domain.form.UserConfigForm;
import br.com.leonardoferreira.jirareport.mapper.UserConfigMapper;
import br.com.leonardoferreira.jirareport.repository.UserConfigRepository;
import br.com.leonardoferreira.jirareport.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Override
    @Transactional(readOnly = true)
    public UserConfigForm myInfo() {
        log.info("Method=myInfo");

        UserConfig userConfig = userConfigRepository.findByUsername(currentUser().getUsername());
        return userConfigMapper.userConfigToForm(userConfig == null ? new UserConfig() : userConfig);
    }

    @Override
    @Transactional
    public void update(final UserConfigForm userConfigForm) {
        log.info("Method=update, userConfigForm={}", userConfigForm);

        UserConfig userConfig = userConfigRepository.findByUsername(currentUser().getUsername());
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

        UserConfig userConfig = userConfigRepository.findByUsername(currentUser().getUsername());
        if (userConfig == null || StringUtils.isEmpty(userConfig.getHolidayToken())) {
            userConfig = new UserConfig();
            userConfig.setCity("ARARAQUARA");
            userConfig.setState("SP");
            userConfig.setHolidayToken(holidayToken);

            return userConfig;
        }

        return userConfig;
    }
}
