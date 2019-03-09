package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.form.IssueForm;
import br.com.jiratorio.domain.SandBoxFilter;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.SandBox;
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
