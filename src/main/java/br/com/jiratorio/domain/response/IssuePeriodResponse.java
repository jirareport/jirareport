package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.IssuePeriodChart;
import java.util.List;

import br.com.jiratorio.domain.entity.IssuePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuePeriodResponse {

    private List<IssuePeriod> issuePeriods;

    private IssuePeriodChart issuePeriodChart;

}
