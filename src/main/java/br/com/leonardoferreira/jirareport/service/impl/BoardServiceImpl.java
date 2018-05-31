package br.com.leonardoferreira.jirareport.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.leonardoferreira.jirareport.client.ProjectClient;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.vo.BoardStatus;
import br.com.leonardoferreira.jirareport.domain.vo.BoardStatusList;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.BoardRepository;
import br.com.leonardoferreira.jirareport.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lferreira
 * @since 7/28/17 11:40 AM
 */
@Slf4j
@Service
public class BoardServiceImpl extends AbstractService implements BoardService {

    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private BoardRepository boardRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Board> findAll() {
        log.info("Method=findAll");

        return (List<Board>) boardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JiraProject> findAllInJira() {
        log.info("Method=findAllInJira");

        return projectClient.findAll(currentToken());
    }

    @Override
    @Transactional
    public void create(final Board board) {
        log.info("Method=create, board={}", board);

        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        log.info("Method=delete, id={}", id);

        boardRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Board findById(final Long id) {
        log.info("Method=findById, id={}", id);

        return boardRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    @Transactional
    public void update(final Board board) {
        log.info("Method=board, board={}", board);

        boardRepository.save(board);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> findStatusFromBoardInJira(final Long boardId) {
        Board board = findById(boardId);
        return findStatusFromBoardInJira(board);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> findStatusFromBoardInJira(final Board board) {
        log.info("Method=findStatusFromBoardInJira, board={}", board);

        List<BoardStatusList> listStatusesBoard = projectClient.findStatusFromProject(currentToken(), board.getExternalId());

        return listStatusesBoard.stream()
                .map(BoardStatusList::getStatuses)
                .flatMap(Collection::stream)
                .map(BoardStatus::getName)
                .collect(Collectors.toSet());
    }
}
