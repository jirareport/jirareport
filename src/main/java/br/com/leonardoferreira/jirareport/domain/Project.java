package br.com.leonardoferreira.jirareport.domain;

import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

/**
 * @author lferreira
 * @since 7/28/17 11:39 AM
 */
@Data
@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
public class Project extends BaseEntity {

    private static final long serialVersionUID = -7981771761592933325L;

    @Id
    private Long id;

    private String name;

    // Avaliar colocar em outro objeto ProjectConfig

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
        Set<String> startColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (startColumn == null) {
            return startColumns;
        }
        startColumns.add(startColumn);
        if (fluxColumn != null && !fluxColumn.isEmpty() && endColumn != null) {
            int start = fluxColumn.indexOf(startColumn);
            int end = fluxColumn.indexOf(endColumn);
            if (start >= 0 && end >= 0 && start < end) {
                startColumns.addAll(fluxColumn.subList(start + 1, end + 1));
            }
        }
        return startColumns;
    }

    @Transient
    public Set<String> getEndColumns() {
        Set<String> endColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (endColumn == null) {
            return endColumns;
        }
        endColumns.add(endColumn);
        if (fluxColumn != null && !fluxColumn.isEmpty()) {
            int start = fluxColumn.indexOf(endColumn);
            if (start >= 0 && start < fluxColumn.size() - 1) {
                endColumns.addAll(fluxColumn.subList(start + 1, fluxColumn.size()));
            }
        }
        return endColumns;
    }

}
