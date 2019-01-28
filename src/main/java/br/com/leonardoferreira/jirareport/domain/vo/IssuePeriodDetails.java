package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssuePeriodDetails {

    private Double avgLeadTime;

    private Integer issueCount;

    private Long boardId;

    private String jql;

    private Double wipAvg;

    private Double avgPctEfficiency;

}
