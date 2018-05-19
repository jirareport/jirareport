package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.Histogram;
import br.com.leonardoferreira.jirareport.domain.vo.LeadTimeCompareChart;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/projects/{projectId}/issues")
public class IssueController extends AbstractController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ChartService chartService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long projectId, final IssueForm issueForm) {
        SandBox sandBox = issueService.findByExample(projectId, issueForm);
        SandBoxFilter sandBoxFilter = issueService.findSandBoxFilters(projectId, sandBox, issueForm);
        Histogram histogramData = issueService.calcHistogramData(sandBox.getIssues());
        Project project = projectService.findById(projectId);
        LeadTimeCompareChart leadTimeCompareChart = chartService.calcLeadTimeCompare(sandBox.getIssues());

        return new ModelAndView("issues/index")
                .addObject("issueForm", issueForm)
                .addObject("project", project)
                .addObject("sandBox", sandBox)
                .addObject("sandBoxFilter", sandBoxFilter)
                .addObject("histogram", histogramData)
                .addObject("leadTimeCompareChart", leadTimeCompareChart);
    }
}
