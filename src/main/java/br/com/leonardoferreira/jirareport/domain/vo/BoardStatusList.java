package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class BoardStatusList {

    private String self;

    private String id;

    private String name;

    private String subtask;

    private List<BoardStatus> statuses;

}
