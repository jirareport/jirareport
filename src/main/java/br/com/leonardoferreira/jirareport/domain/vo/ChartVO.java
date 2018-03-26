package br.com.leonardoferreira.jirareport.domain.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leferreira
 * @since 11/16/17 12:46 PM
 */
@Data
@AllArgsConstructor
public class ChartVO<L, V> {

    private List<L> axisX;

    private List<V> axisY;

    public ChartVO() {
        this.axisX = new ArrayList<>();
        this.axisY = new ArrayList<>();
    }

    public String getAxisXJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(axisX);
    }

    public String getAxisYJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(axisY);
    }

    public boolean getHasData() {
        return !axisX.isEmpty() && !axisY.isEmpty();
    }

    public void add(final L x, final V y) {
        axisX.add(x);
        axisY.add(y);
    }

}
