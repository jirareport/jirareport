package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.Percentile;
import br.com.jiratorio.service.PercentileService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PercentileServiceImpl implements PercentileService {

    @Override
    public Percentile calculatePercentile(final List<Long> leadTimes) {
        log.info("Method=calculatePercentile, leadTimes={}", leadTimes);
        Long median = 0L;
        Long percentile75 = 0L;
        Long percentile90 = 0L;

        double average = leadTimes.parallelStream()
                .filter(Objects::nonNull)
                .mapToLong(i -> i)
                .average().orElse(0D);

        leadTimes.sort(Comparator.naturalOrder());
        if (!leadTimes.isEmpty()) {
            int medianIndex = calculateCeilingPercentage(leadTimes.size(), 50);
            int percentile75Index = calculateCeilingPercentage(leadTimes.size(), 75);
            int percentile90Index = calculateCeilingPercentage(leadTimes.size(), 90);

            median = leadTimes.get(medianIndex - 1);
            percentile75 = leadTimes.get(percentile75Index - 1);
            percentile90 = leadTimes.get(percentile90Index - 1);
        }

        return Percentile.builder()
                .average(average)
                .median(median)
                .percentile75(percentile75)
                .percentile90(percentile90)
                .build();
    }

    private int calculateCeilingPercentage(final int totalElements, final int percentage) {
        return new BigDecimal((double) totalElements * percentage / 100).setScale(0, RoundingMode.CEILING)
                .intValue();
    }

}
