package br.com.jiratorio.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "issue", callSuper = false)
public class LeadTime extends BaseEntity {
    private static final long serialVersionUID = 2918615471478687270L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private LeadTimeConfig leadTimeConfig;

    @ManyToOne
    private Issue issue;

    private Long leadTime;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
