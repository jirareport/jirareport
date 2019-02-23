package br.com.leonardoferreira.jirareport.helper;

import br.com.leonardoferreira.jirareport.domain.ChartType;
import br.com.leonardoferreira.jirareport.domain.DueDateType;
import br.com.leonardoferreira.jirareport.domain.ImpedimentType;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.service.UserService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public String description(final DueDateType dueDateType) {
        switch (dueDateType) {
            case FIRST_AND_LAST_DUE_DATE:
                return "Calcula a diferença entre o primeiro e o último due date";
            case FIRST_DUE_DATE_AND_END_DATE:
                return "Calcula a diferença entre a primeira dueDate e a data de entrega";
            case LAST_DUE_DATE_AND_END_DATE:
                return "Calcula a diferença entre a última dueDate e a data de entrega";
            default:
                throw new IllegalArgumentException();
        }
    }
}
