package br.com.leonardoferreira.jirareport.domain;

import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.domain.embedded.DueDateHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lferreira
 * @since 7/28/17 12:50 PM
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "key")
@EqualsAndHashCode(callSuper = false)
public class Issue extends BaseEntity {
    private static final long serialVersionUID = -1084659211505084402L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String key;

    private String issueType;

    private String creator;

    private String system;

    private String epic;

    private String summary;

    private String estimated;

    private String project;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long leadTime;

    private LocalDateTime created;

    private String priority;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Changelog> changelog;

    @ManyToMany(mappedBy = "issues", cascade = CascadeType.DETACH)
    private List<IssuePeriod> issuePeriods;

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LeadTime> leadTimes;

    @ManyToOne
    private Board board;

    private Long differenceFirstAndLastDueDate;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<DueDateHistory> dueDateHistory;

    private Long impedimentTime;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> dynamicFields;

    private Long waitTime;

    private Long touchTime;

    private Double pctEfficiency;

}
