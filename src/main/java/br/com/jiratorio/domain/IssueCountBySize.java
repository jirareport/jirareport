package br.com.jiratorio.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueCountBySize {

    private Set<String> labels;

    private Map<String, List<Long>> datasources;

    public String getLabelsAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(labels);
    }

    public String getDatasourcesAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(datasources);
    }

    public Boolean getHasData() {
        return labels != null && !labels.isEmpty();
    }
}
