package br.com.leonardoferreira.jirareport.domain.form;

import br.com.leonardoferreira.jirareport.domain.EstimateFieldReference;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EstimateForm {

    private LocalDate startDate;

    private LocalDate endDate;

    private EstimateFieldReference filter;

}
