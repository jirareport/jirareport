package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.vo.HistogramVO;
import br.com.leonardoferreira.jirareport.service.IssueService;
import java.util.List;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChart;
import br.com.leonardoferreira.jirareport.exception.CreateIssuePeriodException;
import br.com.leonardoferreira.jirareport.service.IssuePeriodService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class IssuePeriodController extends AbstractController {

    @Autowired
    private IssuePeriodService issuePeriodService;

    @Autowired
    private IssueService issueService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long projectId) {
        List<IssuePeriod> issuePeriods = issuePeriodService.findByProjectId(projectId);
        IssuePeriodChart issuePeriodChart = issuePeriodService.getChartByIssues(issuePeriods);

        return new ModelAndView("issue-periods/index")
                .addObject("issuePeriods", issuePeriods)
                .addObject("issuePeriodId", new IssuePeriodId(projectId))
                .addObject("issuePeriodChart", issuePeriodChart);
    }

    @GetMapping("/details")
    public ModelAndView details(@PathVariable final Long projectId, final IssuePeriodId issuePeriodId) {
        issuePeriodId.setProjectId(projectId);
        IssuePeriod issuePeriod = issuePeriodService.findById(issuePeriodId);
        HistogramVO histogramData = issueService.findHistogramData(issuePeriod.getIssues());

        return new ModelAndView("issue-periods/details")
                .addObject("issuePeriod", issuePeriod)
                .addObject("histogram", histogramData);
    }

    @PostMapping
    public ModelAndView create(@PathVariable final Long projectId,
                               @Validated final IssuePeriodId issuePeriodId,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        issuePeriodId.setProjectId(projectId);

        if (bindingResult.hasErrors()) {
            List<IssuePeriod> issuePeriods = issuePeriodService.findByProjectId(projectId);
            return new ModelAndView("issue-periods/index")
                    .addObject("issuePeriods", issuePeriods)
                    .addObject("issuePeriodId", issuePeriodId);
        }

        try {
            issuePeriodService.create(issuePeriodId);

            redirectAttributes.addFlashAttribute("flashSuccess", "Registro inserido com sucesso");
            return new ModelAndView(String.format("redirect:/projects/%d/issue-periods", projectId));
        } catch (CreateIssuePeriodException e) {
            List<IssuePeriod> issuePeriods = issuePeriodService.findByProjectId(projectId);
            IssuePeriodChart issuePeriodChart = issuePeriodService.getChartByIssues(issuePeriods);

            return new ModelAndView("issue-periods/index")
                    .addObject("issuePeriods", issuePeriods)
                    .addObject("issuePeriodId", issuePeriodId)
                    .addObject("issuePeriodChart", issuePeriodChart)
                    .addObject("flashError", e.getMessage());
        }
    }

    @PutMapping
    public ModelAndView update(@PathVariable final Long projectId,
                               final IssuePeriodId issuePeriodId,
                               final RedirectAttributes redirectAttributes) {
        issuePeriodId.setProjectId(projectId);
        try {
            issuePeriodService.update(issuePeriodId);
            redirectAttributes.addFlashAttribute("flashSuccess", "Registro atualizado com sucesso.");
        } catch (CreateIssuePeriodException e) {
            redirectAttributes.addFlashAttribute("flashError", "Falha ao atualizar registro.");
        }

        return new ModelAndView(String.format("redirect:/projects/%d/issue-periods", projectId));
    }

    @DeleteMapping
    public ModelAndView remove(@PathVariable final Long projectId,
                               final IssuePeriodId issuePeriodId,
                               final RedirectAttributes redirectAttributes) {
        issuePeriodId.setProjectId(projectId);
        issuePeriodService.remove(issuePeriodId);

        redirectAttributes.addFlashAttribute("flashSuccess", "Registro removido com sucesso.");
        return new ModelAndView(String.format("redirect:/projects/%d/issue-periods", projectId));
    }
}
