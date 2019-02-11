package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.form.IssueForm;
import br.com.jiratorio.domain.vo.SandBoxFilter;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.vo.SandBox;
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
