package br.com.jiratorio.controller;

import br.com.jiratorio.domain.response.IssuePeriodResponse;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.Issue;
import br.com.jiratorio.domain.entity.IssuePeriod;
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest;
import br.com.jiratorio.domain.response.IssuePeriodDetailsResponse;
import br.com.jiratorio.domain.response.ListIssuePeriodResponse;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.IssuePeriodService;
import br.com.jiratorio.service.IssueService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/boards/{boardId}/issue-periods")
public class IssuePeriodController {

    private final IssuePeriodService issuePeriodService;

    private final IssueService issueService;

    private final BoardService boardService;

    public IssuePeriodController(final IssuePeriodService issuePeriodService,
                                 final IssueService issueService,
                                 final BoardService boardService) {
        this.issuePeriodService = issuePeriodService;
        this.issueService = issueService;
        this.boardService = boardService;
    }

    @GetMapping
    public ListIssuePeriodResponse index(@PathVariable final Long boardId) {
        IssuePeriodResponse issuePeriodResponse = issuePeriodService.findIssuePeriodsAndCharts(boardId);

        return ListIssuePeriodResponse.builder()
                .issuePeriods(issuePeriodResponse.getIssuePeriods())
                .issuePeriodChart(issuePeriodResponse.getIssuePeriodChart())
                .board(boardService.findById(boardId))
                .build();
    }

    @GetMapping("/{issuePeriodId}")
    public IssuePeriodDetailsResponse details(@PathVariable final Long boardId,
                                              @PathVariable final Long issuePeriodId) {

        IssuePeriod issuePeriod = issuePeriodService.findById(issuePeriodId);
        List<Issue> issues = issueService.findByIssuePeriodId(issuePeriod.getId());
        Board board = boardService.findById(boardId);

        return IssuePeriodDetailsResponse.builder()
                .board(board)
                .issuePeriod(issuePeriod)
                .issues(issues)
                .build();
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable final Long boardId,
                                    @Valid @RequestBody final CreateIssuePeriodRequest createIssuePeriodRequest) {
        Long id = issuePeriodService.create(createIssuePeriodRequest, boardId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(id);

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{issuePeriodId}")
    public ResponseEntity<?> update(@PathVariable final Long issuePeriodId) {
        issuePeriodService.update(issuePeriodId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{issuePeriodId}")
    public ResponseEntity<?> remove(@PathVariable final Long issuePeriodId) {
        issuePeriodService.remove(issuePeriodId);

        return ResponseEntity.noContent().build();
    }
}
