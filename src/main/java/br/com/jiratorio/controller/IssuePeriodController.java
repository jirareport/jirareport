package br.com.jiratorio.controller;

import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.Issue;
import br.com.jiratorio.domain.IssuePeriod;
import br.com.jiratorio.domain.form.IssuePeriodForm;
import br.com.jiratorio.domain.response.IssuePeriodDetailsResponse;
import br.com.jiratorio.domain.response.ListIssuePeriodResponse;
import br.com.jiratorio.domain.vo.IssuePeriodList;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.IssuePeriodService;
import br.com.jiratorio.service.IssueService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    private IssuePeriodService issuePeriodService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ListIssuePeriodResponse index(@PathVariable final Long boardId) {
        IssuePeriodList issuePeriodList = issuePeriodService.findIssuePeriodsAndCharts(boardId);

        return ListIssuePeriodResponse.builder()
                .issuePeriods(issuePeriodList.getIssuePeriods())
                .issuePeriodChart(issuePeriodList.getIssuePeriodChart())
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
                                    @Validated @RequestBody final IssuePeriodForm issuePeriodForm) {
        Long id = issuePeriodService.create(issuePeriodForm, boardId);
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
