package br.com.leonardoferreira.jirareport.domain;

import br.com.leonardoferreira.jirareport.util.CalcUtil;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

/**
 * @author lferreira
 * @since 7/28/17 11:39 AM
 */
@Data
@Entity
@ToString(exclude = { "leadTimeConfigs" })
@EqualsAndHashCode(of = "id", callSuper = false)
public class Board extends BaseEntity {

    private static final long serialVersionUID = -7981771761592933325L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long externalId;

    private String name;

    private String startColumn;

    private String endColumn;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> fluxColumn;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> ignoreIssueType;

    private String epicCF;

    private String estimateCF;

    private String systemCF;

    private String projectCF;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<LeadTimeConfig> leadTimeConfigs;

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

    @Transient
    public Set<String> getStartColumns() {
        return CalcUtil.calcStartColumns(startColumn, endColumn, fluxColumn);
    }

    @Transient
    public Set<String> getEndColumns() {
        return CalcUtil.calcEndColumns(endColumn, fluxColumn);
    }

}
