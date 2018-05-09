package br.com.leonardoferreira.jirareport.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@EqualsAndHashCode(callSuper = true)
public class Issue extends BaseEntity {
    private static final long serialVersionUID = -1084659211505084402L;

    @Id
    private String key;

    private String creator;

    private String created;

    private String updated;

    private String startDate;

    private String endDate;

    private Long leadTime;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> components;

    private String epic;

    private String summary;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Changelog> changelog;

    private String estimated;

    @Transient
    public String getTitle() {
        return String.format("%s %s", key, summary);
    }

    @Transient
    public String getComponentsStr() {
        return String.join(", ", components);
    }

}
