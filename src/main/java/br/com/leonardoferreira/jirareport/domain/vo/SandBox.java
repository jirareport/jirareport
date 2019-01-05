package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SandBox {

    private List<Issue> issues;

    private ChartAggregator chartAggregator;

    private Double avgLeadTime;

    private Chart<String, Long> weeklyThroughput;

    public SandBox() {
        this.issues = new ArrayList<>();
    }

}
