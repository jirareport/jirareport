package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

/**
 * @author s2it_leferreira
 * @since 5/7/18 7:47 PM
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
