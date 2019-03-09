package br.com.jiratorio.service;

import br.com.jiratorio.domain.Percentile;
import java.util.List;

public interface PercentileService {

    Percentile calculatePercentile(List<Long> leadTimes);

}
