package br.com.leonardoferreira.jirareport.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lferreira
 * @since 7/28/17 11:39 AM
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {
    private static final long serialVersionUID = -7981771761592933325L;

    @Id
    private Long id;

    private String name;

    // Avaliar colocar em outro objeto ProjectConfig

    private String startColumn;

    private String endColumn;

    private String epicCF;

    private String estimateCF;

    private String systemCF;

}
