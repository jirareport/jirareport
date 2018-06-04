package br.com.leonardoferreira.jirareport.controller;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChart;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodList;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.IssuePeriodService;
import br.com.leonardoferreira.jirareport.service.IssueService;
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
@RequestMapping("/boards/{boardId}/issue-periods")
public class IssuePeriodController extends AbstractController {

    @Autowired
    private IssuePeriodService issuePeriodService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long boardId) {
        IssuePeriodList issuePeriodList = issuePeriodService.findIssuePeriodsAndCharts(boardId);
        List<IssuePeriod> issuePeriods = issuePeriodList.getIssuePeriods();
        IssuePeriodChart issuePeriodChart = issuePeriodList.getIssuePeriodChart();
        Board board = boardService.findById(boardId);

        return new ModelAndView("issue-periods/index")
                .addObject("issuePeriods", issuePeriods)
                .addObject("issuePeriodChart", issuePeriodChart)
                .addObject("issuePeriodForm", new IssuePeriodForm())
                .addObject("board", board);
    }

    @GetMapping("/{issuePeriodId}/details")
    public ModelAndView details(@PathVariable final Long boardId,
                                @PathVariable final Long issuePeriodId) {

        IssuePeriod issuePeriod = issuePeriodService.findById(issuePeriodId);
        List<Issue> issues = issueService.findByIssuePeriodId(issuePeriod.getId());
        Board board = boardService.findById(boardId);

        return new ModelAndView("issue-periods/details")
                .addObject("issuePeriod", issuePeriod)
                .addObject("issues", issues)
                .addObject("board", board);
    }

    @PostMapping
    public ModelAndView create(@PathVariable final Long boardId,
            @Validated final IssuePeriodForm issuePeriodForm,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            Board board = boardService.findById(boardId);
            IssuePeriodList issuePeriodList = issuePeriodService.findIssuePeriodsAndCharts(boardId);
            List<IssuePeriod> issuePeriods = issuePeriodList.getIssuePeriods();
            IssuePeriodChart issuePeriodChart = issuePeriodList.getIssuePeriodChart();

            return new ModelAndView("issue-periods/index")
                    .addObject("issuePeriods", issuePeriods)
                    .addObject("issuePeriodForm", issuePeriodForm)
                    .addObject("issuePeriodChart", issuePeriodChart)
                    .addObject("board", board);
        }

        issuePeriodService.create(issuePeriodForm, boardId);

        addFlashSuccess(redirectAttributes, "Registro inserido com sucesso.");
        return new ModelAndView(String.format("redirect:/boards/%d/issue-periods", boardId));
    }

    @PutMapping("/{issuePeriodId}")
    public ModelAndView update(@PathVariable final Long boardId,
            @PathVariable final Long issuePeriodId,
            final RedirectAttributes redirectAttributes) {

        issuePeriodService.update(issuePeriodId);
        addFlashSuccess(redirectAttributes, "Registro atualizado com sucesso.");

        return new ModelAndView(String.format("redirect:/boards/%d/issue-periods", boardId));
    }

    @DeleteMapping("/{issuePeriodId}")
    public ModelAndView remove(@PathVariable final Long boardId,
            @PathVariable final Long issuePeriodId,
            final RedirectAttributes redirectAttributes) {
        issuePeriodService.remove(issuePeriodId);

        addFlashSuccess(redirectAttributes, "Registro removido com sucesso.");
        return new ModelAndView(String.format("redirect:/boards/%d/issue-periods", boardId));
    }
}
