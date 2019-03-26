package br.com.jiratorio.domain.form;

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class IssueForm {

    private LocalDate startDate;

    private LocalDate endDate;

    private List<String> keys = new ArrayList<>();

    private List<String> systems = new ArrayList<>();

    private List<String> taskSize = new ArrayList<>();

    private List<String> epics = new ArrayList<>();

    private List<String> issueTypes = new ArrayList<>();

    private List<String> projects = new ArrayList<>();

    private List<String> priorities = new ArrayList<>();

    private List<DynamicFieldsValues> dynamicFieldsValues = new ArrayList<>();

}
