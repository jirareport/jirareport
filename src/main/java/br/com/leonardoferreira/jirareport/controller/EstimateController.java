package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.EstimateFieldReference;
import br.com.leonardoferreira.jirareport.domain.form.EstimateForm;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.EstimateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/boards/{boardId}/estimate")
public class EstimateController extends AbstractController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private EstimateService estimateService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long boardId, final EstimateForm estimateForm) {

        Board board = boardService.findById(boardId);

        List<EstimateIssue> estimateIssueList = estimateService.findEstimateIssues(boardId, estimateForm);

        List<EstimateFieldReference> estimateFieldReferenceList = EstimateFieldReference.retrieveCustomList(
                StringUtils.isNotEmpty(board.getSystemCF()),
                StringUtils.isNotEmpty(board.getEstimateCF()),
                StringUtils.isNotEmpty(board.getEpicCF()),
                StringUtils.isNotEmpty(board.getProjectCF()));

        if (estimateForm.getStartDate() == null || estimateForm.getEndDate() == null) {
            estimateForm.setStartDate(LocalDate.now().minusMonths(4));
            estimateForm.setEndDate(LocalDate.now());
        }

        return new ModelAndView("estimate/index")
                .addObject("estimateForm", estimateForm)
                .addObject("board", board)
                .addObject("estimateFieldReferenceList", estimateFieldReferenceList)
                .addObject("estimateIssueList", estimateIssueList);
    }
}
