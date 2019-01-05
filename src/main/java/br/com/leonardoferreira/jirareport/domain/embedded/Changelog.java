package br.com.leonardoferreira.jirareport.domain.embedded;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Changelog implements Serializable {
    private static final long serialVersionUID = 6432821107545516780L;

    private LocalDateTime created;

    private String from;

    private String to;

    private Long leadTime;

    private LocalDateTime endDate;
}
