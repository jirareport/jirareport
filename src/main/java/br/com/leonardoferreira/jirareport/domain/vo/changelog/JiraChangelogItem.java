package br.com.leonardoferreira.jirareport.domain.vo.changelog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author s2it_leferreira
 * @since 11/06/18 13:48
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraChangelogItem {

    private String field;

    private String fieldtype;

    private String from;

    private String fromString;

    private String to;

    private String toString;

    @JsonIgnore
    private LocalDateTime created;

}
