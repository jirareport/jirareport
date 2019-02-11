package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.EstimateFieldReference;
import br.com.jiratorio.domain.form.EstimateForm;
import br.com.jiratorio.domain.vo.EstimateIssue;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateResponse {

    private EstimateForm estimateForm;

    private Board board;

    private List<EstimateFieldReference> estimateFieldReferenceList;

    private List<EstimateIssue> estimateIssueList;

}
