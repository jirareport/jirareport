package br.com.jiratorio.controller;

import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.form.IssueForm;
import br.com.jiratorio.domain.response.ListIssueResponse;
import br.com.jiratorio.domain.vo.SandBox;
import br.com.jiratorio.domain.vo.SandBoxFilter;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.IssueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/issues")
public class IssueController {

    private final IssueService issueService;

    private final BoardService boardService;

    public IssueController(final IssueService issueService, final BoardService boardService) {
        this.issueService = issueService;
        this.boardService = boardService;
    }

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
