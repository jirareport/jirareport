package br.com.leonardoferreira.jirareport.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class LeadTimeCompareChart implements Serializable {
    private static final long serialVersionUID = -1501002922104599319L;

    private List<String> labels;

    private Map<String, List<Double>> datasources;

    public LeadTimeCompareChart() {
        this.labels = new ArrayList<>();
        this.datasources = new HashMap<>();
    }

    public String getLabelsAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(labels);
    }

    public String getDatasourcesAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(datasources);
    }

    public Boolean getHasData() {
        return labels != null && !labels.isEmpty();
    }

    public void add(final String key, final Map<String, Double> collect) {
        labels.add(key);
        collect.forEach((k, v) -> {
            if (datasources.containsKey(k)) {
                final List<Double> data = datasources.get(k);
                data.add(v);

                datasources.put(k, data);
            } else {
                List<Double> data = new ArrayList<>();
                data.add(v);

                datasources.put(k, data);
            }
        });
    }
}
