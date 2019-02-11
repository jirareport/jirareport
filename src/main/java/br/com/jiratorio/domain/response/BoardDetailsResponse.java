package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.vo.DynamicFieldConfig;
import br.com.jiratorio.domain.ImpedimentType;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = "name")
public class BoardDetailsResponse {

    private Long externalId;

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

    private ImpedimentType impedimentType;

    private List<String> impedimentColumns;

    private List<DynamicFieldConfig> dynamicFields;

}
