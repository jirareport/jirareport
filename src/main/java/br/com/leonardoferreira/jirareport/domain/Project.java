package br.com.leonardoferreira.jirareport.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author leferreira
 * @since 7/28/17 11:39 AM
 */
@Data
public class Project {

    @Id
    private Integer id;

    private String name;

    // Avaliar colocar em outro objeto ProjectConfig

    private String startColumn;

    private String endColumn;
}
