package br.com.jiratorio.domain.form;

import br.com.jiratorio.util.DateUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuePeriodForm {

    @NotNull(message = "Data de inicio é obrigatória")
    private LocalDate startDate;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate endDate;

    @AssertFalse(message = "O período não pode ser maior que 31 dias. Para maiores períodos utilize o SandBox")
    public boolean isValidRange() {
        return startDate != null && endDate != null && ChronoUnit.DAYS.between(startDate, endDate) > 31;
    }

    @AssertTrue(message = "A data de inicio deve ser anterior a data fim.")
    public boolean isStartDateIsBeforeEndDate() {
        return startDate == null || endDate == null || startDate.isBefore(endDate) || startDate.isEqual(endDate);
    }

    public LocalDate getStartDate() {
        return startDate == null ? DateUtil.firstMonthDay() : startDate;
    }

    public LocalDate getEndDate() {
        return endDate == null ? DateUtil.lastMonthDay() : endDate;
    }
}
