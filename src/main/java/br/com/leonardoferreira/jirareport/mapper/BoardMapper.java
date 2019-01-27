package br.com.leonardoferreira.jirareport.mapper;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.request.CreateBoardRequest;
import br.com.leonardoferreira.jirareport.domain.request.UpdateBoardRequest;
import br.com.leonardoferreira.jirareport.domain.response.BoardDetailsResponse;
import br.com.leonardoferreira.jirareport.domain.response.BoardResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {

    BoardDetailsResponse toBoardResponseDetails(Board board);

    Board boardFromCreateBoardRequest(CreateBoardRequest request);

    List<BoardResponse> toBoardResponse(List<Board> boards);

    default Page<BoardResponse> toBoardResponse(Page<Board> boards) {
        List<BoardResponse> boardResponses = toBoardResponse(boards.getContent());
        return new PageImpl<>(boardResponses, boards.getPageable(), boards.getTotalElements());
    }

    void fromUpdateBoardRequest(@MappingTarget Board board, UpdateBoardRequest updateBoardRequest);
}
