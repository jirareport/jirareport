package br.com.leonardoferreira.jirareport.domain.embedded;

import java.beans.Transient;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnTimeAvg implements Serializable {
    private static final long serialVersionUID = -3791819163293059573L;

    private String columnName;

    private Double avgTime;

    @Transient
    public String getLeadTime() {
        return String.format("%.2f", avgTime);
    }
}
