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
            @Mapping(target = "id",              ignore = true),
            @Mapping(target = "description",     source = "holidayVO.name"),
            @Mapping(target = "board.id",        source = "boardId"),
            @Mapping(target = "date",            source = "holidayVO.date", dateFormat = "dd/MM/yyyy"),
            @Mapping(target = "owner",           ignore = true),
            @Mapping(target = "lastEditor",      ignore = true),
            @Mapping(target = "createdAt",       ignore = true),
            @Mapping(target = "updatedAt",       ignore = true)
    })
    Holiday fromVO(HolidayVO holidayVO, Long boardId);

    default List<Holiday> fromVOS(List<HolidayVO> holidayVOS, Long boardId) {
        return holidayVOS.stream()
                .map(holidayVO -> fromVO(holidayVO, boardId))
                .collect(Collectors.toList());
    }
}
