package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Board;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/boards/{boardId}/estimate")
public class EstimateController extends AbstractController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private EstimateService estimateService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long boardId, final EstimateForm estimateForm){

        Board board = boardService.findById(boardId);

        List<EstimateIssue> estimateIssueList = estimateService.findEstimateIssues(boardId, estimateForm);

        Map<String, String> filterMap = getFilterMap(board);

        if (estimateForm.getStartDate() == null || estimateForm.getEndDate() == null) {
            estimateForm.setStartDate(LocalDate.now().minusMonths(4));
            estimateForm.setEndDate(LocalDate.now());
        }

        return new ModelAndView("estimate/index")
                .addObject("estimateForm", estimateForm)
                .addObject("board", board)
                .addObject("filterMap", filterMap)
                .addObject("estimateIssueList", estimateIssueList);

    }

    private Map<String, String> getFilterMap(Board board) {
        Map<String,String> map = new HashMap<>();
        map.put("issueType","Tipo de issue");

        if (StringUtils.isNotEmpty(board.getSystemCF())){
            map.put("system", "Sistema");
        }
        if (StringUtils.isNotEmpty(board.getEstimateCF())){
            map.put("taskSize", "Tamanho/estimativa");
        }
        if (StringUtils.isNotEmpty(board.getEpicCF())){
            map.put("epic", "Épico");
        }
        if (StringUtils.isNotEmpty(board.getProjectCF())){
            map.put("project", "Projeto");
        }
        map.put("prority", "Prioridade");
        return map;
    }
}
