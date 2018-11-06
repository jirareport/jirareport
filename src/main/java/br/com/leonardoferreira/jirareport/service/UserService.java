package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.ChartType;
import br.com.leonardoferreira.jirareport.domain.UserConfig;
import br.com.leonardoferreira.jirareport.domain.form.UserConfigForm;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    void update(UserConfigForm userConfigForm);

    UserConfigForm myInfo();

    UserConfig findHolidayInfo();

    ChartType retrieveFavoriteLeadTimeChartType();

    ChartType retrieveFavoriteThroughputChartType();

    UserConfig retrieveCurrentUserConfig();

}
