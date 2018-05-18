package br.com.leonardoferreira.jirareport.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

/**
 * @author lferreira
 * @since 7/28/17 12:50 PM
 */
@Data
@Entity
@ToString(of = "key")
@EqualsAndHashCode(callSuper = false)
public class Issue extends BaseEntity {
    private static final long serialVersionUID = -1084659211505084402L;

    @Id
    private String key;

    private String issueType;

    private String creator;

    private String system;

    private String epic;

    private String summary;

    private String estimated;

    private String project;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Changelog> changelog;

    @ManyToMany(mappedBy = "issues", cascade = CascadeType.ALL)
    private List<IssuePeriod> issuePeriods;

    @OneToMany(mappedBy = "issue", fetch = FetchType.EAGER)
    private List<LeadTime> leadTimes;

    @Transient
    public String getTitle() {
        return String.format("%s %s", key, summary);
    }

    @Transient
    public String getStartDate() {
        return leadTimes.get(0).getStartDate();
    }

    @Transient
    public String getEndDate() {
        return leadTimes.get(0).getEndDate();
    }

    @Transient
    public Long getLeadTime() {
        return leadTimes.get(0).getLeadTime();
    }

}
