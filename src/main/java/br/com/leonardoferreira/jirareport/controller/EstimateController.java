package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.EstimateForm;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.EstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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


        return new ModelAndView("estimate/index")
                .addObject("estimateForm", estimateForm)
                .addObject("board", board)
                .addObject("estimate", null);

    }
}
