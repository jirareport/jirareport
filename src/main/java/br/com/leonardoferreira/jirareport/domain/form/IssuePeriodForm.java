package br.com.leonardoferreira.jirareport.domain.form;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lferreira on 31/05/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuePeriodForm {

    @NotNull(message = "Data de inicio é obrigatória")
    private LocalDate startDate;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate endDate;

    public LocalDate getStartDate() {
        if (startDate == null) {
            startDate = DateUtil.firstMonthDay();
        }
        return startDate;
    }

    public LocalDate getEndDate() {
        if (endDate == null) {
            endDate = DateUtil.lastMonthDay();
        }
        return endDate;
    }
}
