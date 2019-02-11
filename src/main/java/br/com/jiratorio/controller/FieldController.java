package br.com.jiratorio.controller;

import br.com.jiratorio.domain.vo.JiraField;
import br.com.jiratorio.service.FieldService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @GetMapping
    public List<JiraField> fields() {
        return fieldService.findAllJiraFields();
    }

}
