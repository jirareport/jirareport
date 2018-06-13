package br.com.leonardoferreira.jirareport.domain.vo.changelog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/**
 * @author s2it_leferreira
 * @since 11/06/18 13:41
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraChangelog {

    private Long startAt;

    private Long maxResults;

    private Long total;

    private List<JiraChangelogHistory> histories;
}
