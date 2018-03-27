package br.com.leonardoferreira.jirareport.domain;

import lombok.Data;

/**
 * @author lferreira
 * @since 7/28/17 1:03 PM
 */
@Data
public class Changelog {

    private String created;

    private String from;

    private String to;

    private Long cycleTime;
}
