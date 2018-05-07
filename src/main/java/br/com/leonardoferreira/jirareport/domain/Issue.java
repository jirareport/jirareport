package br.com.leonardoferreira.jirareport.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

/**
 * @author lferreira
 * @since 7/28/17 12:50 PM
 */
@Data
@ToString(of = "key")
public class Issue {

    @Id
    @Indexed(unique = true)
    private String key;

    private String creator;

    private String created;

    private String updated;

    private String startDate;

    private String endDate;

    private Long leadTime;

    private List<String> components;

    private String epic;

    private String summary;

    private List<Changelog> changelog;

    private String estimated;

    public String getTitle() {
        return String.format("%s %s", key, summary);
    }

    public String getComponentsStr() {
        return String.join(", ", components);
    }

}
