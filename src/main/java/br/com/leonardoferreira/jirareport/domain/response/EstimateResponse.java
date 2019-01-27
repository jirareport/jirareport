package br.com.leonardoferreira.jirareport.domain.response;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.EstimateFieldReference;
import br.com.leonardoferreira.jirareport.domain.form.EstimateForm;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;
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
