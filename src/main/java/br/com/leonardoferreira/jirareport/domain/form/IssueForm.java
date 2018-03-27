package br.com.leonardoferreira.jirareport.domain.form;

import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lferreira
 * @since 8/1/17 12:43 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueForm implements Serializable {

    @NotEmpty(message = "Data inicial não pode ser vazia.")
    private String startDate;

    @NotEmpty(message = "Data final não pode ser vazia.")
    private String endDate;

    private Long projectId;

    public IssueForm(final Long projectId) {
        this.projectId = projectId;
    }

    public String getStartDate() {
        if (startDate == null) {
            startDate = DateUtil.firstMonthDay();
        }
        return startDate;
    }

    public String getEndDate() {
        if (endDate == null) {
            endDate = DateUtil.lastMonthDay();
        }
        return endDate;
    }

    public String getId() {
        return String.format("%d [%s - %s]", projectId, startDate, endDate);
    }

    public String getDates() {
        return String.format("[%s - %s]", startDate, endDate);
    }
}
