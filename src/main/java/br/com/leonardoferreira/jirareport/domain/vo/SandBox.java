package br.com.leonardoferreira.jirareport.domain.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author lferreira
 * @since 5/11/18 4:25 PM
 */
@Data
@Builder
@AllArgsConstructor
public class SandBox {

    private List<Issue> issues;

    private ChartAggregator chartAggregator;

    private Double avgLeadTime;

    public SandBox() {
        this.issues = new ArrayList<>();
    }

    public String getLeadTime() {
        return String.format("%.2f", avgLeadTime);
    }

}
