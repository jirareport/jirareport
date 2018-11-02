package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.LeadTime;
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
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "key")
@EqualsAndHashCode(callSuper = false)
public class EstimateIssue {
    private String key;

    private String issueType;

    private String creator;

    private String system;

    private String epic;

    private String summary;

    private String estimated;

    private String project;

    private LocalDateTime startDate;

    private LocalDate estimateDateAvg;

    private LocalDate estimateDatePercentile50;

    private LocalDate estimateDatePercentile75;

    private LocalDate estimateDatePercentile90;

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

    private Long impedimentTime;}
