package br.com.jiratorio.mapper;

import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.Holiday;
import br.com.jiratorio.domain.request.HolidayRequest;
import br.com.jiratorio.domain.response.HolidayResponse;
import br.com.jiratorio.domain.vo.HolidayVO;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring")
public interface HolidayMapper {

    @Mappings({ // @formatter:off
            @Mapping(target = "id",              ignore = true),
            @Mapping(target = "description",     source = "holidayVO.name"),
            @Mapping(target = "board.id",        source = "boardId"),
            @Mapping(target = "date",            source = "holidayVO.date", dateFormat = "dd/MM/yyyy"),
            @Mapping(target = "owner",           ignore = true),
            @Mapping(target = "lastEditor",      ignore = true),
            @Mapping(target = "createdAt",       ignore = true),
            @Mapping(target = "updatedAt",       ignore = true)
    }) // @formatter:on
    Holiday fromVO(HolidayVO holidayVO, Long boardId);

    default List<Holiday> fromVOS(List<HolidayVO> holidayVOS, Long boardId) {
        return holidayVOS.stream()
                .map(holidayVO -> fromVO(holidayVO, boardId))
                .collect(Collectors.toList());
    }

    @Mappings({ // @formatter:off
            @Mapping(target = "date",    dateFormat = "dd/MM/yyyy"),
            @Mapping(target = "boardId", source = "board.id")
    }) // @formatter:on
    HolidayResponse toHolidayResponse(Holiday holidays);

    List<HolidayResponse> toHolidayResponse(List<Holiday> holidays);

    default Page<HolidayResponse> toHolidayResponse(Page<Holiday> holidays) {
        List<HolidayResponse> holidayResponses = toHolidayResponse(holidays.getContent());
        return new PageImpl<>(holidayResponses, holidays.getPageable(), holidays.getTotalElements());
    }

    @Mappings({ // @formatter:off
            @Mapping(target = "id",              ignore = true),
            @Mapping(target = "board.id",        source = "board.id"),
            @Mapping(target = "description",     source = "holidayRequest.description"),
            @Mapping(target = "date",            source = "holidayRequest.date", dateFormat = "dd/MM/yyyy"),
            @Mapping(target = "owner",           ignore = true),
            @Mapping(target = "lastEditor",      ignore = true),
            @Mapping(target = "createdAt",       ignore = true),
            @Mapping(target = "updatedAt",       ignore = true)
    }) // @formatter:off
    Holiday toHoliday(HolidayRequest holidayRequest, Board board);

    @Mappings({ // @formatter:off
            @Mapping(target = "id",              ignore = true),
            @Mapping(target = "board.id",        ignore = true),
            @Mapping(target = "date",            dateFormat = "dd/MM/yyyy"),
            @Mapping(target = "owner",           ignore = true),
            @Mapping(target = "lastEditor",      ignore = true),
            @Mapping(target = "createdAt",       ignore = true),
            @Mapping(target = "updatedAt",       ignore = true)
    }) // @formatter:off
    void updateFromRequest(@MappingTarget Holiday holiday, HolidayRequest holidayRequest);

}
