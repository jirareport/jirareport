package br.com.leonardoferreira.jirareport.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnTimeAvg {

    private String columnName;

    private Double avgTime;

    public String getLeadTime() {
        return String.format("%.2f", avgTime);
    }
}
