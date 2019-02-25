package br.com.leonardoferreira.jirareport.domain;

import br.com.leonardoferreira.jirareport.domain.vo.DynamicFieldConfig;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@ToString(exclude = {"leadTimeConfigs", "holidays", "issues"})
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

    @Column(name = "DUE_DATE_CF")
    private String dueDateCF;

    private Boolean ignoreWeekend;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<LeadTimeConfig> leadTimeConfigs;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Holiday> holidays;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Issue> issues;

    @Enumerated(EnumType.STRING)
    private ImpedimentType impedimentType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> impedimentColumns;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<DynamicFieldConfig> dynamicFields;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> touchingColumns;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> waitingColumns;

    @Enumerated(EnumType.STRING)
    private DueDateType dueDateType;

}
