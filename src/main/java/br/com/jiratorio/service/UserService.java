package br.com.jiratorio.service;

import br.com.jiratorio.domain.ImportHolidayInfo;
import br.com.jiratorio.domain.entity.UserConfig;
import br.com.jiratorio.domain.form.UserConfigForm;

public interface UserService {

    void update(UserConfigForm userConfigForm);

    UserConfigForm myInfo();

    ImportHolidayInfo retrieveHolidayInfo();

    UserConfig retrieveCurrentUserConfig();

}
