package br.com.leonardoferreira.jirareport.domain.request;

import lombok.Data;

@Data
public class CreateBoardRequest {

    private String name;

    private Long externalId;

}
