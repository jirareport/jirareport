package br.com.leonardoferreira.jirareport.domain.embedded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lferreira
 * @since 11/16/17 12:46 PM
 */
@Data
@AllArgsConstructor
public class Chart<L, V> implements Serializable {
    private static final long serialVersionUID = 7550041573002395950L;

    private List<L> axisX;

    private List<V> axisY;

    public Chart() {
        this.axisX = new ArrayList<>();
        this.axisY = new ArrayList<>();
    }

    @Transient
    public String getAxisXJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(axisX);
    }

    @Transient
    public String getAxisYJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(axisY);
    }

    @Transient
    public boolean getHasData() {
        return !axisX.isEmpty() && !axisY.isEmpty();
    }

    public void add(final L x, final V y) {
        axisX.add(x);
        axisY.add(y);
    }

}
