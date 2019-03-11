package br.com.jiratorio.domain.impediment.calculator;

import br.com.jiratorio.domain.entity.embedded.Changelog;
import java.util.List;

public class ImpedimentCalculatorByColumn {

    public Long timeInImpediment(final List<Changelog> changelog, final List<String> impedimentColumns) {
        return changelog.stream()
                .filter(cl -> impedimentColumns.contains(cl.getTo()) && cl.getLeadTime() != null)
                .mapToLong(Changelog::getLeadTime)
                .sum();
    }

}
