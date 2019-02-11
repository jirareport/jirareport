package br.com.jiratorio.domain.response;

import lombok.Data;

@Data
public class HolidayResponse {

    private Long id;

    private String date;

    private String description;

    private Long boardId;

}
