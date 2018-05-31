package br.com.leonardoferreira.jirareport.domain.embedded;

import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lferreira
 * @since 8/1/17 12:43 PM
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class IssuePeriodId implements Serializable {
    private static final long serialVersionUID = -6443572350367677008L;

    @NotEmpty(message = "Data inicial não pode ser vazia.")
    private String startDate;

    @NotEmpty(message = "Data final não pode ser vazia.")
    private String endDate;

    private Long boardId;

    public IssuePeriodId(final Long boardId) {
        this.boardId = boardId;
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
        return String.format("%d [%s - %s]", boardId, startDate, endDate);
    }

    public String getDates() {
        return String.format("[%s - %s]", startDate, endDate);
    }
}
