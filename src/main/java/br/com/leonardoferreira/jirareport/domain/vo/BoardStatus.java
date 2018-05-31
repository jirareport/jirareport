package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

/**
 * @author s2it_jsilveira
 */
@Data
public class BoardStatus {

    private String self;

    private String description;

    private String iconUrl;

    private String name;

    private String id;

    private StatusCategory statusCategory;

}
