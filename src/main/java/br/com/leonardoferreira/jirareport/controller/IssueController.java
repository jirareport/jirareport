package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.Histogram;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/boards/{boardId}/issues")
public class IssueController extends AbstractController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ModelAndView index(@PathVariable final Long boardId, final IssueForm issueForm) {
        SandBox sandBox = issueService.findByExample(boardId, issueForm);
        SandBoxFilter sandBoxFilter = issueService.findSandBoxFilters(boardId, sandBox, issueForm);
        Histogram histogramData = issueService.calcHistogramData(sandBox.getIssues());
        Board board = boardService.findById(boardId);

        return new ModelAndView("issues/index")
                .addObject("issueForm", issueForm)
                .addObject("board", board)
                .addObject("sandBox", sandBox)
                .addObject("sandBoxFilter", sandBoxFilter)
                .addObject("histogram", histogramData);
    }
}
