package br.com.jiratorio.domain;

import br.com.jiratorio.domain.entity.embedded.Chart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicChart implements Serializable {
    private static final long serialVersionUID = 5429746843879854333L;

    private String name;

    private Chart<String, Double> leadTime;

    private Chart<String, Long> throughput;

    @JsonIgnore
    public String getId() {
        return name == null ? null : name.toLowerCase().replaceAll(" ", "");
    }

}
