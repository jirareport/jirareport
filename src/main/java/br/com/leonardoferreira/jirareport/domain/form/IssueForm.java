package br.com.leonardoferreira.jirareport.domain.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class IssueForm {

    private Date startDate;

    private Date endDate;

    private List<String> keys = new ArrayList<>();

    private List<String> systems = new ArrayList<>();

    private List<String> taskSize = new ArrayList<>();

    private List<String> epics = new ArrayList<>();

    private List<String> issueTypes = new ArrayList<>();

    private List<String> projects = new ArrayList<>();

}
