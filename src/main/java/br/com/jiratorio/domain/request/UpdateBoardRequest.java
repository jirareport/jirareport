package br.com.jiratorio.domain.request;

import br.com.jiratorio.domain.DueDateType;
import br.com.jiratorio.domain.impediment.ImpedimentType;
import br.com.jiratorio.domain.DynamicFieldConfig;
import java.util.List;
import lombok.Data;

@Data
public class UpdateBoardRequest {

    private String name;

    private String startColumn;

    private String endColumn;

    private List<String> fluxColumn;

    private List<String> ignoreIssueType;

    private String epicCF;

    private String estimateCF;

    private String systemCF;

    private String projectCF;

    private Boolean ignoreWeekend;

    private ImpedimentType impedimentType;

    private List<String> impedimentColumns;

    private List<DynamicFieldConfig> dynamicFields;

    private List<String> touchingColumns;

    private List<String> waitingColumns;

    private String dueDateCF;

    private DueDateType dueDateType;

}
