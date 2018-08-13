package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.UserConfig;
import br.com.leonardoferreira.jirareport.domain.form.UserConfigForm;

public interface UserService {

    void update(UserConfigForm userConfigForm);

    UserConfigForm myInfo();

    UserConfig findHolidayInfo();

}
