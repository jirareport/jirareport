package br.com.leonardoferreira.jirareport.controller;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.service.IssueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/projects/{projectId}/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(final IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public ModelAndView index(@PathVariable final Long projectId, final IssueForm issueForm) {
        List<Issue> issues = issueService.findByExample(projectId, issueForm);

        return new ModelAndView("issues/index")
                .addObject("issueForm", issueForm)
                .addObject("projectId", projectId)
                .addObject("issues", issues);
    }
}
