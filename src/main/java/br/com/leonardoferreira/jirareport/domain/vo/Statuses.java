package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

/**
 * @author jsilveira
 */
@Data
public class Statuses {

    private String self;
    private String description;
    private String iconUrl;
    private String name;
    private String id;
    private StatusCategory statusCategory;

}
