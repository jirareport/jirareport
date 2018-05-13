package br.com.leonardoferreira.jirareport.builder;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;

public class HolidayBuilder {

    public static Holiday builderVoToEntity(HolidayVO vo, Project project) {
        return Holiday.builder().date(vo.getDate()).description(vo.getName()).project(project).build();
    }

    public static Holiday builderVoToEntity(HolidayVO vo) {
        return Holiday.builder().date(vo.getDate()).description(vo.getName()).build();
    }

}
