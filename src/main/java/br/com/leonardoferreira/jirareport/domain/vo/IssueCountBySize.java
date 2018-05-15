package br.com.leonardoferreira.jirareport.domain.vo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class IssueCountBySize {

    private Set<String> labels;

    private Map<String, List<Long>> datasources;

    public String getLabelsAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(labels);
    }

    public String getDatasourcesAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(datasources);
    }

    public boolean getHasData() {
        return labels != null && !labels.isEmpty();
    }
}
