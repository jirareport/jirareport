package br.com.leonardoferreira.jirareport.domain.request;

import br.com.leonardoferreira.jirareport.domain.ImpedimentType;
import br.com.leonardoferreira.jirareport.domain.vo.DynamicFieldConfig;
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

    private Boolean calcDueDate;

    private Boolean ignoreWeekend;

    private ImpedimentType impedimentType;

    private List<String> impedimentColumns;

    private List<DynamicFieldConfig> dynamicFields;

    private List<String> touchingColumns;

    private List<String> waitingColumns;

}
