package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.Issue;
import java.util.List;
import lombok.Data;

/**
 * @author s2it_leferreira
 * @since 5/11/18 4:25 PM
 */
@Data
public class SandBox {

    private List<Issue> issues;

    private ChartAggregator chartAggregator;

    private Double avgLeadTime;

    public String getLeadTime() {
        return String.format("%.2f", avgLeadTime);
    }

}
