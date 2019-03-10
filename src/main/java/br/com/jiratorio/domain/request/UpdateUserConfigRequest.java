package br.com.jiratorio.domain.request;

import br.com.jiratorio.domain.ChartType;
import lombok.Data;

@Data
public class UpdateUserConfigRequest {

    private String state;

    private String city;

    private String holidayToken;

    private ChartType leadTimeChartType;

    private ChartType throughputChartType;

}
