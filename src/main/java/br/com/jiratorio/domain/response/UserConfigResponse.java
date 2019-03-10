package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.ChartType;
import lombok.Data;

@Data
public class UserConfigResponse {

    private String state;

    private String city;

    private String holidayToken;

    private String leadTimeChartType = ChartType.BAR.name();

    private String throughputChartType = ChartType.DOUGHNUT.name();

}
