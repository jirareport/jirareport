package br.com.leonardoferreira.jirareport.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface HolidayMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "description", source = "holidayVO.name"),
            @Mapping(target = "project.id", source = "projectId"),
            @Mapping(target = "date", source = "holidayVO.date", dateFormat = "dd/MM/yyyy")
    })
    Holiday fromVO(HolidayVO holidayVO, Long projectId);

    default List<Holiday> fromVOS(List<HolidayVO> holidayVOS, Long projectId) {
        return holidayVOS.stream()
                .map(holidayVO -> fromVO(holidayVO, projectId))
                .collect(Collectors.toList());
    }
}
