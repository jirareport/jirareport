package br.com.leonardoferreira.jirareport.domain.form;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class EstimateForm {

    private LocalDate startDate;

    private LocalDate endDate;

}
