package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

/**
 * @author lferreira
 * @since 7/28/17 11:02 AM
 */
@Data
public class HolidayVO {

    private String date;
    private String name;
    private String link;
    private String type;
    private String description;
    private String type_code;

}
