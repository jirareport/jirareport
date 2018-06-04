package br.com.leonardoferreira.jirareport.domain;

import br.com.leonardoferreira.jirareport.util.CalcUtil;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author lferreira on 17/05/18
 */
@Data
@Entity
@ToString(exclude = { "board" })
@EqualsAndHashCode(exclude = "board", callSuper = false)
public class LeadTimeConfig extends BaseEntity {
    private static final long serialVersionUID = -1181175426509346889L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    @NotEmpty(message = "Nome não pode ser vazio.")
    private String name;

    @NotEmpty(message = "Coluna de inicio não pode ser vazio")
    private String startColumn;

    @NotEmpty(message = "Coluna de fim não pode ser vazio")
    private String endColumn;

    @Transient
    public Set<String> getStartColumns() {
        return CalcUtil.calcStartColumns(startColumn, endColumn, board.getFluxColumn());
    }

    @Transient
    public Set<String> getEndColumns() {
        return CalcUtil.calcEndColumns(endColumn, board.getFluxColumn());
    }

    public void setStartColumn(final String startColumn) {
        this.startColumn = startColumn == null ? null : startColumn.toUpperCase(DateUtil.LOCALE_BR);
    }

    public void setEndColumn(final String endColumn) {
        this.endColumn = endColumn == null ? null : endColumn.toUpperCase(DateUtil.LOCALE_BR);
    }
}
