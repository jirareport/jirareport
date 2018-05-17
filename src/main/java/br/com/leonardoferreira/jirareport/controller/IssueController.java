package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.HistogramVO;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import br.com.leonardoferreira.jirareport.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/projects/{projectId}/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long projectId, final IssueForm issueForm) {
        SandBox sandBox = issueService.findByExample(projectId, issueForm);
        SandBoxFilter sandBoxFilter = issueService.findSandBoxFilters(projectId, sandBox, issueForm);
        HistogramVO histogramData = issueService.calcHistogramData(sandBox.getIssues());

        return new ModelAndView("issues/index")
                .addObject("issueForm", issueForm)
                .addObject("projectId", projectId)
                .addObject("sandBox", sandBox)
                .addObject("sandBoxFilter", sandBoxFilter)
                .addObject("histogram", histogramData);
    }
}
