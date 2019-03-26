package br.com.jiratorio.controller;

import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.estimate.EstimateFieldReference;
import br.com.jiratorio.domain.form.EstimateForm;
import br.com.jiratorio.domain.response.EstimateResponse;
import br.com.jiratorio.domain.estimate.EstimateIssue;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.EstimateService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/estimates")
public class EstimateController {

    private final BoardService boardService;

    private final EstimateService estimateService;

    public EstimateController(final BoardService boardService,
                              final EstimateService estimateService) {
        this.boardService = boardService;
        this.estimateService = estimateService;
    }

    @GetMapping
    public EstimateResponse index(@PathVariable final Long boardId, final EstimateForm estimateForm) {
        Board board = boardService.findById(boardId);

        List<EstimateIssue> estimateIssueList = estimateService.findEstimateIssues(boardId, estimateForm);

        List<EstimateFieldReference> estimateFieldReferenceList = EstimateFieldReference.retrieveCustomList(
                !StringUtils.isEmpty(board.getSystemCF()),
                !StringUtils.isEmpty(board.getEstimateCF()),
                !StringUtils.isEmpty(board.getEpicCF()),
                !StringUtils.isEmpty(board.getProjectCF()));

        if (estimateForm.getStartDate() == null || estimateForm.getEndDate() == null) {
            estimateForm.setStartDate(LocalDate.now().minusMonths(4));
            estimateForm.setEndDate(LocalDate.now());
        }

        return EstimateResponse.builder()
                .estimateForm(estimateForm)
                .board(board)
                .estimateFieldReferenceList(estimateFieldReferenceList)
                .estimateIssueList(estimateIssueList)
                .build();
    }
}
