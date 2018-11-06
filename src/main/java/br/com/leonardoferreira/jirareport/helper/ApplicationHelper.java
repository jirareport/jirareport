package br.com.leonardoferreira.jirareport.helper;

import br.com.leonardoferreira.jirareport.domain.ChartType;
import br.com.leonardoferreira.jirareport.domain.ImpedimentType;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author lferreira on 31/05/18
 */
@Component
public class ApplicationHelper implements Helper {

    @Autowired
    private UserService userService;

    @Override
    public String getName() {
        return "applicationHelper";
    }

    public String issueTitle(final Issue issue) {
        return String.format("%s %s", issue.getKey(), issue.getSummary());
    }

    public String fmtLeadTime(final Double leadTime) {
        return String.format("%.2f", leadTime);
    }

    public String fmtLocalDate(final LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public ImpedimentType[] impedimentTypes() {
        return ImpedimentType.values();
    }

    public String retrieveFavoriteLeadTimeChartType() {
        ChartType chartType = userService.retrieveFavoriteLeadTimeChartType();
        return chartType == null ? "bar" : chartType.chartName();
    }

    public String retrieveFavoriteThroughputChartType() {
        ChartType chartType = userService.retrieveFavoriteThroughputChartType();
        return chartType == null ? "doughnut" : chartType.chartName();
    }
}
