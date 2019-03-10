package br.com.jiratorio.service;

import br.com.jiratorio.domain.ImportHolidayInfo;
import br.com.jiratorio.domain.request.UpdateUserConfigRequest;
import br.com.jiratorio.domain.response.UserConfigResponse;

public interface UserConfigService {

    void update(String username, UpdateUserConfigRequest updateUserConfigRequest);

    ImportHolidayInfo retrieveHolidayInfo(String username);

    UserConfigResponse findByUsername(String username);

}
