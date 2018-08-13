package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.HolidayClient;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.UserConfig;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.mapper.HolidayMapper;
import br.com.leonardoferreira.jirareport.repository.HolidayRepository;
import br.com.leonardoferreira.jirareport.repository.BoardRepository;
import br.com.leonardoferreira.jirareport.service.HolidayService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.leonardoferreira.jirareport.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lferreira
 * @since 5/7/18 6:52 PM
 */
@Slf4j
@Service
public class HolidayServiceImpl extends AbstractService implements HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private HolidayClient holidayClient;

    @Autowired
    private HolidayMapper holidayMapper;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<Holiday> findByBoard(final Long boardId, final Pageable pageable) {
        log.info("Method=findByBoard, boardId={}", boardId);

        return holidayRepository.findAllByBoardId(boardId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Holiday> findByBoard(final Long boardId) {
        log.info("Method=findByBoard, boardId={}", boardId);

        return holidayRepository.findAllByBoardId(boardId);
    }

    @Override
    @Transactional
    public void create(final Long boardId, final Holiday holiday) {
        log.info("Method=create, holiday={}", holiday);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(ResourceNotFound::new);
        holiday.setBoard(board);
        holidayRepository.save(holiday);
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        log.info("Method=delete, id={}", id);

        holidayRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Holiday findById(final Long id) {
        log.info("Method=findById, id={}", id);

        return holidayRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    @Transactional
    public void update(final Long boardId, final Holiday holiday) {
        log.info("Method=update, holiday={}", holiday);

        final Board board = boardRepository.findById(boardId)
                .orElseThrow(ResourceNotFound::new);

        holiday.setBoard(board);
        holidayRepository.save(holiday);
    }

    @Override
    @Transactional
    public boolean createImported(final Long boardId) {
        log.info("Method=createImported, boardId={}", boardId);

        List<Holiday> holidaysByBoard = holidayRepository.findAllByBoardId(boardId);
        List<HolidayVO> allHolidaysVOInCity = findAllHolidaysInCity();
        List<Holiday> allHolidaysInCity = holidayMapper.fromVOS(allHolidaysVOInCity, boardId);

        if (holidaysByBoard.containsAll(allHolidaysInCity)) {
            return false;
        }

        Set<Holiday> holidays = new HashSet<>(holidaysByBoard);
        holidays.addAll(allHolidaysInCity);

        try {
            holidayRepository.saveAll(holidays);
        } catch (DataIntegrityViolationException e) {
            log.error("Method=createImported, e={}", e.getMessage(), e);
            return false;
        }

        return true;
    }

    private List<HolidayVO> findAllHolidaysInCity() {
        UserConfig userConfig = userService.findHolidayInfo();
        return holidayClient.findAllHolidaysInCity(LocalDate.now().getYear(),
                userConfig.getState(), userConfig.getCity(), userConfig.getHolidayToken());
    }
}
