package br.com.leonardoferreira.jirareport.domain.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class IssueForm {

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    private List<String> keys = new ArrayList<>();

    private List<String> systems = new ArrayList<>();

    private List<String> taskSize = new ArrayList<>();

    private List<String> epics = new ArrayList<>();

    private List<String> issueTypes = new ArrayList<>();

    private List<String> projects = new ArrayList<>();

}
