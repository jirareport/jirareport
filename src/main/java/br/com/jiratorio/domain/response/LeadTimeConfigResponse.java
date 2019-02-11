package br.com.jiratorio.domain.response;

import lombok.Data;

@Data
public class LeadTimeConfigResponse {

    private Long id;

    private Long boardId;

    private String name;

    private String startColumn;

    private String endColumn;

}
