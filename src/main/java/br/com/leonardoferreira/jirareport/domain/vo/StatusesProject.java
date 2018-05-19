package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jsilveira
 */
@Data
public class StatusesProject {

    private String self;
    private String id;
    private String name;
    private String subtask;
    private List<Statuses> statuses;

}
