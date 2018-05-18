package br.com.leonardoferreira.jirareport.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * @author lferreira on 17/05/18
 */
@Data
@Entity
public class LeadTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private LeadTimeConfig leadTimeConfig;

    @ManyToOne
    private Issue issue;

    private Long leadTime;

    private String startDate;

    private String endDate;

}
