package br.com.leonardoferreira.jirareport.service.impl;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.repository.LeadTimeConfigRepository;
import br.com.leonardoferreira.jirareport.service.LeadTimeConfigService;
import br.com.leonardoferreira.jirareport.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lferreira on 17/05/18
 */
@Slf4j
@Service
public class LeadTimeConfigServiceImpl extends AbstractService implements LeadTimeConfigService {

    @Autowired
    private LeadTimeConfigRepository leadTimeConfigRepository;

    @Autowired
    private BoardService boardService;

    @Override
    @Transactional(readOnly = true)
    public List<LeadTimeConfig> findAllByBoardId(final Long boardId) {
        log.info("Method=findAllByBoardId, boardId={}", boardId);

        return leadTimeConfigRepository.findByBoardId(boardId);
    }

    @Override
    @Transactional
    public void create(final Long boardId, final LeadTimeConfig leadTimeConfig) {
        log.info("Method=create, boardId={}, leadTimeConfig={}", boardId, leadTimeConfig);

        Board board = boardService.findById(boardId);
        leadTimeConfig.setBoard(board);

        leadTimeConfigRepository.save(leadTimeConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadTimeConfig findByBoardAndId(final Long boardId, final Long id) {
        log.info("Method=findByBoardAndId, boardId={}, id={}", boardId, id);

        return leadTimeConfigRepository.findByIdAndBoardId(id, boardId);
    }

    @Override
    @Transactional
    public void update(final Long boardId, final LeadTimeConfig leadTimeConfig) {
        log.info("Method=update, boardId={}, leadTimeConfig={}", boardId, leadTimeConfig);

        Board board = boardService.findById(boardId);
        leadTimeConfig.setBoard(board);

        leadTimeConfigRepository.save(leadTimeConfig);
    }

    @Override
    @Transactional
    public void deleteByBoardAndId(final Long boardId, final Long id) {
        log.info("Method=deleteByBoardAndId, boardId={}, id={}", boardId, id);

        leadTimeConfigRepository.deleteByIdAndBoardId(id, boardId);
    }
}
