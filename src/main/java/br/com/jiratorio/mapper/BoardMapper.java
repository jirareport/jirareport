package br.com.jiratorio.mapper;

import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.request.CreateBoardRequest;
import br.com.jiratorio.domain.request.UpdateBoardRequest;
import br.com.jiratorio.domain.response.BoardDetailsResponse;
import br.com.jiratorio.domain.response.BoardResponse;
import br.com.jiratorio.mapper.transformer.StringTransformer;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = StringTransformer.class)
public interface BoardMapper {

    BoardDetailsResponse toBoardResponseDetails(Board board);

    Board boardFromCreateBoardRequest(CreateBoardRequest request);

    List<BoardResponse> toBoardResponse(List<Board> boards);

    default Page<BoardResponse> toBoardResponse(Page<Board> boards) {
        List<BoardResponse> boardResponses = toBoardResponse(boards.getContent());
        return new PageImpl<>(boardResponses, boards.getPageable(), boards.getTotalElements());
    }

    @Mappings({ // @formatter:off
        @Mapping(target = "startColumn",     qualifiedByName = "toUpperCase"),
        @Mapping(target = "endColumn",       qualifiedByName = "toUpperCase"),
        @Mapping(target = "fluxColumn",      qualifiedByName = "listToUpperCase"),
        @Mapping(target = "touchingColumns", qualifiedByName = "listToUpperCase"),
        @Mapping(target = "waitingColumns",  qualifiedByName = "listToUpperCase")
    }) // @formatter:on
    void fromUpdateBoardRequest(@MappingTarget Board board, UpdateBoardRequest updateBoardRequest);
}
