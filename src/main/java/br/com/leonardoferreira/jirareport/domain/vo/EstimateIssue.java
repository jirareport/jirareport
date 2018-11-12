package br.com.leonardoferreira.jirareport.domain.vo;

import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "key")
public class EstimateIssue {
    private String key;

    private String issueType;

    private String creator;

    private String system;

    private String epic;

    private String summary;

    private String estimated;

    private String project;

    private LocalDateTime startDate;

    private LocalDate estimateDateAvg;

    private LocalDate estimateDatePercentile50;

    private LocalDate estimateDatePercentile75;

    private LocalDate estimateDatePercentile90;

    private Long leadTime;

    private LocalDateTime created;

    private String priority;

    private List<Changelog> changelog;

    private Long impedimentTime;
}
