package br.com.leonardoferreira.jirareport.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.client.ProjectClient;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.form.BoardForm;
import br.com.leonardoferreira.jirareport.domain.vo.BoardStatus;
import br.com.leonardoferreira.jirareport.domain.vo.BoardStatusList;
import br.com.leonardoferreira.jirareport.domain.vo.JiraProject;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.mapper.BoardMapper;
import br.com.leonardoferreira.jirareport.repository.BoardRepository;
import br.com.leonardoferreira.jirareport.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class BoardServiceImpl extends AbstractService implements BoardService {

    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMapper boardMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<Board> findAll(final Pageable pageable, final Board board) {
        log.info("Method=findAll");

        if (StringUtils.isEmpty(board.getOwner())) {
            board.setOwner(currentUser().getUsername());
        }

        if ("all".equals(board.getOwner())) {
            board.setOwner(null);
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("owner", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Board> example = Example.of(board, matcher);

        return boardRepository.findAll(example, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JiraProject> findAllJiraProject() {
        log.info("Method=findAllJiraProject");

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

        boardRepository.deleteByIdAndOwner(id, currentUser().getUsername());
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
    public void update(final BoardForm boardForm) {
        log.info("Method=board, boardForm={}", boardForm);

        Board board = findById(boardForm.getId());
        boardMapper.fromForm(board, boardForm);
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

    @Override
    @Transactional(readOnly = true)
    public BoardForm findToUpdate(final Long id) {
        log.info("Method=findToUpdate, id={}", id);

        Board board = findById(id);
        JiraProject jiraProject = projectClient.findById(currentToken(), board.getExternalId());

        return boardMapper.toForm(board, jiraProject);
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public List<String> findAllOwners() {
        log.info("Method=findAllOwners");
        return boardRepository.findAllOwners();
    }
}
