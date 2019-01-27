package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.request.UpdateBoardRequest;
import br.com.leonardoferreira.jirareport.domain.response.BoardDetailsResponse;
import br.com.leonardoferreira.jirareport.domain.request.CreateBoardRequest;
import br.com.leonardoferreira.jirareport.domain.response.BoardResponse;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    Page<BoardResponse> findAll(Pageable pageable, Board board);

    Long create(CreateBoardRequest board);

    void delete(Long id);

    Board findById(Long id);

    void update(Long boardId, UpdateBoardRequest updateBoardRequest);

    Set<String> findStatusFromBoardInJira(Board board);

    Set<String> findStatusFromBoardInJira(Long boardId);

    BoardDetailsResponse findDetailsById(Long id);

    List<String> findAllOwners();

}
