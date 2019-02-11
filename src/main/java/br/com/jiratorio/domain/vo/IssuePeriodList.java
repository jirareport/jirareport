package br.com.jiratorio.domain.vo;

import java.util.List;

import br.com.jiratorio.domain.IssuePeriod;
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
