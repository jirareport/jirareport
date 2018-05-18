package br.com.leonardoferreira.jirareport.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * @author lferreira on 17/05/18
 */
@Data
@Entity
public class LeadTimeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    @NotEmpty(message = "Nome não pode ser vazio.")
    private String name;

    @NotEmpty(message = "Coluna de inicio não pode ser vazio")
    private String startColumn;

    @NotEmpty(message = "Coluna de fim não pode ser vazio")
    private String endColumn;

}
