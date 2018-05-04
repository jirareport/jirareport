package br.com.leonardoferreira.jirareport.controller;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.IssueResult;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.IssueResultChartVO;
import br.com.leonardoferreira.jirareport.exception.CreateIssueResultException;
import br.com.leonardoferreira.jirareport.service.IssueResultService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author lferreira
 * @since 11/14/17 5:18 PM
 */
@Controller
public class IssueResultController {

    private final IssueResultService issueResultService;

    public IssueResultController(IssueResultService issueResultService) {
        this.issueResultService = issueResultService;
    }

    @GetMapping("/projects/{projectId}/issues")
    public ModelAndView index(@PathVariable final Long projectId) {
        List<IssueResult> issues = issueResultService.findByProjectId(projectId);
        IssueResultChartVO issueResultChart = issueResultService.getChartByIssues(issues);

        return new ModelAndView("issues/filter")
                .addObject("issues", issues)
                .addObject("issueForm", new IssueForm(projectId))
                .addObject("issueResultChart", issueResultChart);
    }

    @DeleteMapping("/projects/{projectId}/issues/details")
    public ModelAndView remove(@PathVariable final Long projectId,
                               final IssueForm issueForm,
                               final RedirectAttributes redirectAttributes) {
        issueForm.setProjectId(projectId);
        issueResultService.remove(issueForm);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro removido com sucesso.");
        return new ModelAndView(String.format("redirect:/projects/%d/issues", projectId));
    }

    @GetMapping("/projects/{projectId}/issues/details")
    public ModelAndView details(@PathVariable final Long projectId, final IssueForm issueForm) {
        issueForm.setProjectId(projectId);
        IssueResult issue = issueResultService.findById(issueForm);

        return new ModelAndView("issues/index")
                .addObject("issue", issue);
    }

    @PostMapping("/projects/{projectId}/issues")
    public ModelAndView create(@PathVariable final Long projectId,
                               @Validated final IssueForm issueForm,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        issueForm.setProjectId(projectId);

        if (bindingResult.hasErrors()) {
            List<IssueResult> issues = issueResultService.findByProjectId(projectId);
            return new ModelAndView("issues/filter")
                    .addObject("issues", issues)
                    .addObject("issueForm", issueForm);
        }

        try {
            issueResultService.create(issueForm);

            redirectAttributes.addFlashAttribute("flashSuccess", "Registro inserido com sucesso");
            return new ModelAndView(String.format("redirect:/projects/%d/issues", projectId));
        } catch (CreateIssueResultException e) {
            List<IssueResult> issues = issueResultService.findByProjectId(projectId);
            IssueResultChartVO issueResultChart = issueResultService.getChartByIssues(issues);

            return new ModelAndView("issues/filter")
                    .addObject("issues", issues)
                    .addObject("issueForm", issueForm)
                    .addObject("issueResultChart", issueResultChart)
                    .addObject("flashError", e.getMessage());
        }
    }

    @PutMapping("/projects/{projectId}/issues/details")
    public ModelAndView update(@PathVariable final Long projectId,
                               final IssueForm issueForm,
                               final RedirectAttributes redirectAttributes) {
        issueForm.setProjectId(projectId);
        try {
            issueResultService.update(issueForm);
            redirectAttributes.addFlashAttribute("flashSuccess", "Registro atualizado com sucesso.");
        } catch (CreateIssueResultException e) {
            redirectAttributes.addFlashAttribute("flashError", "Falha ao atualizar registro.");
        }

        return new ModelAndView(String.format("redirect:/projects/%d/issues", projectId));
    }
}
