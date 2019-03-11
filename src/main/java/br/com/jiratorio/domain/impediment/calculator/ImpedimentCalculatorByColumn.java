package br.com.jiratorio.domain.impediment.calculator;

import br.com.jiratorio.domain.entity.embedded.Changelog;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ImpedimentCalculatorByColumn {

    private List<Changelog> changelog;

    private List<String> impedimentColumns;

    public Long timeInImpediment() {
        return changelog.stream()
                .filter(cl -> impedimentColumns.contains(cl.getTo()) && cl.getLeadTime() != null)
                .mapToLong(Changelog::getLeadTime)
                .sum();
    }

}
