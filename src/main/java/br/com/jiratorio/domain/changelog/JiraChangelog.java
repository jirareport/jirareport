package br.com.jiratorio.domain.changelog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraChangelog {

    private Long startAt;

    private Long maxResults;

    private Long total;

    private List<JiraChangelogHistory> histories;
}
