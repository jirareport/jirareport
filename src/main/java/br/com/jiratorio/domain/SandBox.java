package br.com.jiratorio.domain;

import br.com.jiratorio.domain.entity.embedded.Chart;
import br.com.jiratorio.domain.entity.Issue;
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
