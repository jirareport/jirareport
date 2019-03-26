package br.com.jiratorio.domain.form;

import br.com.jiratorio.domain.estimate.EstimateFieldReference;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EstimateForm {

    private LocalDate startDate;

    private LocalDate endDate;

    private EstimateFieldReference filter;

}
