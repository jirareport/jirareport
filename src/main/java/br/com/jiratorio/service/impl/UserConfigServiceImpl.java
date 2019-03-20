package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.ImportHolidayInfo;
import br.com.jiratorio.domain.entity.UserConfig;
import br.com.jiratorio.domain.request.UpdateUserConfigRequest;
import br.com.jiratorio.domain.response.UserConfigResponse;
import br.com.jiratorio.mapper.UserConfigMapper;
import br.com.jiratorio.repository.UserConfigRepository;
import br.com.jiratorio.service.UserConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserConfigServiceImpl implements UserConfigService {

    private final UserConfigRepository userConfigRepository;

    private final UserConfigMapper userConfigMapper;

    private final String holidayToken;

    public UserConfigServiceImpl(final UserConfigRepository userConfigRepository,
                                 final UserConfigMapper userConfigMapper,
                                 @Value("${holiday.token}") final String holidayToken) {
        this.userConfigRepository = userConfigRepository;
        this.userConfigMapper = userConfigMapper;
        this.holidayToken = holidayToken;
    }

    @Override
    @Transactional
    public void update(final String username, final UpdateUserConfigRequest updateUserConfigRequest) {
        log.info("Method=update, username={}, updateUserConfigRequest={}", username, updateUserConfigRequest);

        UserConfig userConfig = userConfigRepository.findByUsername(username)
                .orElseGet(() -> {
                    UserConfig uc = new UserConfig();
                    uc.setUsername(username);
                    return uc;
                });

        userConfigMapper.updateFromRequest(userConfig, updateUserConfigRequest);

        userConfigRepository.save(userConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public ImportHolidayInfo retrieveHolidayInfo(final String username) {
        log.info("Method=retrieveHolidayInfo, username={}", username);

        return userConfigRepository.findByUsername(username)
                .filter(uc -> !StringUtils.isEmpty(uc.getHolidayToken()))
                .map(userConfigMapper::toImportHolidayInfo)
                .orElse(new ImportHolidayInfo("SP", "ARARAQUARA", holidayToken));
    }

    @Override
    public UserConfigResponse findByUsername(final String username) {
        log.info("Method=findByUsername, username={}", username);

        return userConfigRepository.findByUsername(username)
                .map(userConfigMapper::userConfigToResponse)
                .orElse(new UserConfigResponse());
    }

}
