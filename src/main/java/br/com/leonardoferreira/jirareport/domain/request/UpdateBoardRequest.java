package br.com.leonardoferreira.jirareport.domain.request;

import br.com.leonardoferreira.jirareport.domain.ImpedimentType;
import br.com.leonardoferreira.jirareport.domain.vo.DynamicFieldConfig;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.util.List;
import java.util.stream.Collectors;
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

    public void setTouchingColumns(final List<String> touchingColumns) {
        this.touchingColumns = touchingColumns == null ? null : touchingColumns.stream()
                .map(String::toUpperCase).collect(Collectors.toList());
    }

    public void setWaitingColumns(final List<String> waitingColumns) {
        this.waitingColumns = waitingColumns == null ? null : waitingColumns.stream()
                .map(String::toUpperCase).collect(Collectors.toList());
    }
}
