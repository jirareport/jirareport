package br.com.leonardoferreira.jirareport.domain.vo;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuePeriodList {

    private List<IssuePeriod> issuePeriods;

    private IssuePeriodChart issuePeriodChart;

}
