package br.com.leonardoferreira.jirareport.domain.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;

/**
 * @author lferreira
 * @since 5/18/18 8:09 PM
 */
@Data
public class LeadTimeCompareChart {

    private Set<String> labels;

    private Map<String, List<Long>> datasources;

    public LeadTimeCompareChart() {
        this.labels = new HashSet<>();
        this.datasources = new HashMap<>();
    }

    public String getLabelsAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(labels);
    }

    public String getDatasourcesAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(datasources);
    }

    public boolean getHasData() {
        return labels != null && !labels.isEmpty();
    }

    public void add(final String key, final Map<String, Long> collect) {
        labels.add(key);
        collect.forEach((k, v) -> {
            if (datasources.containsKey(k)) {
                final List<Long> data = datasources.get(k);
                data.add(v);

                datasources.put(k, data);
            } else {
                List<Long> data = new ArrayList<>();
                data.add(v);

                datasources.put(k, data);
            }
        });
    }
}
