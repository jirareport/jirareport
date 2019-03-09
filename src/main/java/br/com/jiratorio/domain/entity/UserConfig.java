package br.com.jiratorio.domain.entity;

import br.com.jiratorio.domain.ChartType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(of = "username", callSuper = false)
public class UserConfig extends BaseEntity {

    private static final long serialVersionUID = -9168105728096346993L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String state;

    private String city;

    private String holidayToken;

    @Enumerated(EnumType.STRING)
    private ChartType leadTimeChartType;

    @Enumerated(EnumType.STRING)
    private ChartType throughputChartType;

}
