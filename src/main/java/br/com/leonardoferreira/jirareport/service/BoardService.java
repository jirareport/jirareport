package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;

import java.util.List;
import java.util.Set;

/**
 * Created by lferreira on 3/26/18
 */
public interface BoardService {

    List<Board> findAll();

    List<JiraProject> findAllInJira();

    void create(Board board);

    void delete(Long id);

    Board findById(Long id);

    void update(Board board);

    Set<String> findStatusFromBoardInJira(Board board);

    Set<String> findStatusFromBoardInJira(Long boardId);
}
