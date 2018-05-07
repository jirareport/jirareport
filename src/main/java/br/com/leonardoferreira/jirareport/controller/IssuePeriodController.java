package br.com.leonardoferreira.jirareport.controller;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChartVO;
import br.com.leonardoferreira.jirareport.exception.CreateIssuePeriodException;
import br.com.leonardoferreira.jirareport.service.IssuePeriodService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author lferreira
 * @since 11/14/17 5:18 PM
 */
@Controller
@RequestMapping("/projects/{projectId}/issue-periods")
public class IssuePeriodController {

    private final IssuePeriodService issuePeriodService;

    public IssuePeriodController(IssuePeriodService issuePeriodService) {
        this.issuePeriodService = issuePeriodService;
    }

    @GetMapping
    public ModelAndView index(@PathVariable final Long projectId) {
        List<IssuePeriod> issuePeriods = issuePeriodService.findByProjectId(projectId);
        IssuePeriodChartVO issuePeriodChart = issuePeriodService.getChartByIssues(issuePeriods);

        return new ModelAndView("issue-periods/index")
                .addObject("issuePeriods", issuePeriods)
                .addObject("issueForm", new IssueForm(projectId))
                .addObject("issuePeriodChart", issuePeriodChart);
    }

    @GetMapping("/details")
    public ModelAndView details(@PathVariable final Long projectId, final IssueForm issueForm) {
        issueForm.setProjectId(projectId);
        IssuePeriod issuePeriod = issuePeriodService.findById(issueForm);

        return new ModelAndView("issue-periods/details")
                .addObject("issuePeriod", issuePeriod);
    }

    @PostMapping
    public ModelAndView create(@PathVariable final Long projectId,
                               @Validated final IssueForm issueForm,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        issueForm.setProjectId(projectId);

        if (bindingResult.hasErrors()) {
            List<IssuePeriod> issuePeriods = issuePeriodService.findByProjectId(projectId);
            return new ModelAndView("issue-periods/index")
                    .addObject("issuePeriods", issuePeriods)
                    .addObject("issueForm", issueForm);
        }

        try {
            issuePeriodService.create(issueForm);

            redirectAttributes.addFlashAttribute("flashSuccess", "Registro inserido com sucesso");
            return new ModelAndView(String.format("redirect:/projects/%d/issue-periods", projectId));
        } catch (CreateIssuePeriodException e) {
            List<IssuePeriod> issuePeriods = issuePeriodService.findByProjectId(projectId);
            IssuePeriodChartVO issuePeriodChart = issuePeriodService.getChartByIssues(issuePeriods);

            return new ModelAndView("issue-periods/index")
                    .addObject("issuePeriods", issuePeriods)
                    .addObject("issueForm", issueForm)
                    .addObject("issuePeriodChart", issuePeriodChart)
                    .addObject("flashError", e.getMessage());
        }
    }

    @PutMapping
    public ModelAndView update(@PathVariable final Long projectId,
                               final IssueForm issueForm,
                               final RedirectAttributes redirectAttributes) {
        issueForm.setProjectId(projectId);
        try {
            issuePeriodService.update(issueForm);
            redirectAttributes.addFlashAttribute("flashSuccess", "Registro atualizado com sucesso.");
        } catch (CreateIssuePeriodException e) {
            redirectAttributes.addFlashAttribute("flashError", "Falha ao atualizar registro.");
        }

        return new ModelAndView(String.format("redirect:/projects/%d/issue-periods", projectId));
    }

    @DeleteMapping
    public ModelAndView remove(@PathVariable final Long projectId,
                               final IssueForm issueForm,
                               final RedirectAttributes redirectAttributes) {
        issueForm.setProjectId(projectId);
        issuePeriodService.remove(issueForm);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro removido com sucesso.");
        return new ModelAndView(String.format("redirect:/projects/%d/issue-periods", projectId));
    }
}
