package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.response.ListIssueResponse;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ListIssueResponse index(@PathVariable final Long boardId, final IssueForm issueForm) {
        SandBox sandBox = issueService.findByExample(boardId, issueForm);
        SandBoxFilter sandBoxFilter = issueService.findSandBoxFilters(boardId, sandBox, issueForm);
        Board board = boardService.findById(boardId);

        return ListIssueResponse.builder()
                .issueForm(issueForm)
                .board(board)
                .sandBox(sandBox)
                .sandBoxFilter(sandBoxFilter)
                .build();
    }
}
