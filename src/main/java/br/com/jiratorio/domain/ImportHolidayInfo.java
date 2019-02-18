package br.com.jiratorio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportHolidayInfo {

    private String state;

    private String city;

    private String holidayToken;

}
