package br.com.leonardoferreira.jirareport.domain.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.beans.Transient;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Chart<L, V> implements Serializable {
    private static final long serialVersionUID = 7550041573002395950L;

    private Map<L, V> data;

    public Chart() {
        data = new LinkedHashMap<>();
    }

    @Transient
    public String getAxisXJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(data.keySet());
    }

    @Transient
    public String getAxisYJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(data.values());
    }

    @Transient
    public Boolean getHasData() {
        return data != null && !data.keySet().isEmpty();
    }

    public void add(final L x, final V y) {
        data.put(x, y);
    }

}
