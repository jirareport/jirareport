package br.com.jiratorio.domain;

import br.com.jiratorio.domain.vo.DynamicFieldConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
@ToString(exclude = { "leadTimeConfigs", "holidays", "issues" })
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

    private Boolean calcDueDate;

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

}
