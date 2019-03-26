package br.com.jiratorio.controller;

import br.com.jiratorio.domain.jira.JiraField;
import br.com.jiratorio.service.FieldService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fields")
public class FieldController {

    private final FieldService fieldService;

    public FieldController(final FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @GetMapping
    public List<JiraField> fields() {
        return fieldService.findAllJiraFields();
    }

}
