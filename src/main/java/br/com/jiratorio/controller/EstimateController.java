package br.com.jiratorio.controller;

import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.EstimateFieldReference;
import br.com.jiratorio.domain.form.EstimateForm;
import br.com.jiratorio.domain.response.EstimateResponse;
import br.com.jiratorio.domain.vo.EstimateIssue;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.EstimateService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/estimates")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EstimateController {

    private final BoardService boardService;

    private final EstimateService estimateService;

    @GetMapping
    public EstimateResponse index(@PathVariable final Long boardId, final EstimateForm estimateForm) {
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

        return EstimateResponse.builder()
                .estimateForm(estimateForm)
                .board(board)
                .estimateFieldReferenceList(estimateFieldReferenceList)
                .estimateIssueList(estimateIssueList)
                .build();
    }
}
