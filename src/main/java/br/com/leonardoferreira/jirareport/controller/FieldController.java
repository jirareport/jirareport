package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.vo.JiraField;
import br.com.leonardoferreira.jirareport.service.FieldService;
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
