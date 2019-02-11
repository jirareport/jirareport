package br.com.jiratorio.service;

import br.com.jiratorio.domain.ChartType;
import br.com.jiratorio.domain.UserConfig;
import br.com.jiratorio.domain.form.UserConfigForm;

public interface UserService {

    void update(UserConfigForm userConfigForm);

    UserConfigForm myInfo();

    UserConfig findHolidayInfo();

    ChartType retrieveFavoriteLeadTimeChartType();

    ChartType retrieveFavoriteThroughputChartType();

    UserConfig retrieveCurrentUserConfig();

}
