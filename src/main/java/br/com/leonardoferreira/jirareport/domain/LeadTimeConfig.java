package br.com.leonardoferreira.jirareport.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = { "board" })
@EqualsAndHashCode(exclude = "board", callSuper = false)
public class LeadTimeConfig extends BaseEntity {
    private static final long serialVersionUID = -1181175426509346889L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    private String name;

    private String startColumn;

    private String endColumn;

}
