package br.com.leonardoferreira.jirareport.domain.form;

import br.com.leonardoferreira.jirareport.domain.ImpedimentType;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.ToString;

/**
 * @author lferreira on 31/05/18
 */
@Data
@ToString(of = "name")
public class BoardForm {

    private Long id;

    private String name;

    private String startColumn;

    private String endColumn;

    private List<String> fluxColumn;

    private List<String> ignoreIssueType;

    private String epicCF;

    private String estimateCF;

    private String systemCF;

    private String projectCF;

    private Boolean calcDueDate;

    private Boolean ignoreWeekend;

    private JiraProject jiraProject;

    private ImpedimentType impedimentType;

    private List<String> impedimentColumns;

    private String usedToEstimatedCF;

    public void setStartColumn(final String startColumn) {
        this.startColumn = startColumn == null ? null : startColumn.toUpperCase(DateUtil.LOCALE_BR);
    }

    public void setEndColumn(final String endColumn) {
        this.endColumn = endColumn == null ? null : endColumn.toUpperCase(DateUtil.LOCALE_BR);
    }

    public void setFluxColumn(final List<String> fluxColumn) {
        this.fluxColumn =
                fluxColumn == null ? null : fluxColumn.stream().map(String::toUpperCase).collect(Collectors.toList());
    }

}
