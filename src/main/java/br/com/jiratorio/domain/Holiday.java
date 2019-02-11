package br.com.jiratorio.domain;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "board")
@EqualsAndHashCode(of = {"date", "board"}, callSuper = false)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"date", "board_id"}))
public class Holiday extends BaseEntity {
    private static final long serialVersionUID = 18640912961216513L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String description;

    @ManyToOne
    private Board board;

}
