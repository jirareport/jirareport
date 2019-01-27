package br.com.leonardoferreira.jirareport.domain.response;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListIssueResponse {

    private IssueForm issueForm;

    private Board board;

    private SandBox sandBox;

    private SandBoxFilter sandBoxFilter;

}
