package br.com.jiratorio.domain.form;

import br.com.jiratorio.domain.ChartType;
import br.com.jiratorio.util.DateUtil;
import br.com.jiratorio.util.StringUtil;
import lombok.Data;

@Data
public class UserConfigForm {

    private String state;

    private String city;

    private String holidayToken;

    private ChartType leadTimeChartType;

    private ChartType throughputChartType;

    public String getCity() {
        if (this.city == null) {
            return null;
        }

        return StringUtil.stripAccents(this.city)
                .replaceAll(" ", "_")
                .toUpperCase(DateUtil.LOCALE_BR);
    }

}
