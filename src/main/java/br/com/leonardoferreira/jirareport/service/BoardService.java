package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.request.CreateBoardRequest;
import java.util.List;
import java.util.Set;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.BoardForm;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    Page<Board> findAll(Pageable pageable, Board board);

    List<JiraProject> findAllJiraProject();

    Long create(CreateBoardRequest board);

    void delete(Long id);

    Board findById(Long id);

    void update(BoardForm board);

    Set<String> findStatusFromBoardInJira(Board board);

    Set<String> findStatusFromBoardInJira(Long boardId);

    BoardForm findToUpdate(Long id);

    List<String> findAllOwners();

}
