package br.com.leonardoferreira.jirareport.domain.form;

import br.com.leonardoferreira.jirareport.util.DateUtil;
import br.com.leonardoferreira.jirareport.util.StringUtil;
import lombok.Data;

@Data
public class UserConfigForm {

    private String state;

    private String city;

    private String holidayToken;

    private String leadTimeChartType;

    private String throughputChartType;

    public String getCity() {
        if (this.city == null) {
            return null;
        }

        return StringUtil.stripAccents(this.city)
                .replaceAll(" ", "_")
                .toUpperCase(DateUtil.LOCALE_BR);
    }

}
