package br.com.leonardoferreira.jirareport.domain.embedded;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author lferreira
 * @since 7/28/17 1:03 PM
 */
@Data
public class Changelog implements Serializable {
    private static final long serialVersionUID = 6432821107545516780L;

    private LocalDateTime created;

    private String from;

    private String to;

    private Long leadTime;
}
