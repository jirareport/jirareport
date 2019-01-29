package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.HolidayClient;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.UserConfig;
import br.com.leonardoferreira.jirareport.domain.request.HolidayRequest;
import br.com.leonardoferreira.jirareport.domain.response.HolidayResponse;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import br.com.leonardoferreira.jirareport.exception.HolidaysAlreadyImported;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.mapper.HolidayMapper;
import br.com.leonardoferreira.jirareport.repository.HolidayRepository;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.service.UserService;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class HolidayServiceImpl extends AbstractService implements HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private HolidayClient holidayClient;

    @Autowired
    private HolidayMapper holidayMapper;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<HolidayResponse> findByBoard(final Long boardId, final Pageable pageable) {
        log.info("Method=findByBoard, boardId={}", boardId);

        Page<Holiday> holidays = holidayRepository.findAllByBoardId(boardId, pageable);
        return holidayMapper.toHolidayResponse(holidays);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> findDaysByBoard(final Long boardId) {
        log.info("Method=findDaysByBoard, boardId={}", boardId);
        return holidayRepository.findAllByBoardId(boardId)
                .stream()
                .map(Holiday::getDate)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long create(final Long boardId, final HolidayRequest holidayRequest) {
        log.info("Method=create, boardId={}, holidayRequest={}", boardId, holidayRequest);

        Board board = boardService.findById(boardId);

        Holiday holiday = holidayMapper.toHoliday(holidayRequest, board);
        holidayRepository.save(holiday);

        return holiday.getId();
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        log.info("Method=delete, id={}", id);

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
        holidayRepository.delete(holiday);
    }

    @Override
    @Transactional(readOnly = true)
    public HolidayResponse findById(final Long id) {
        log.info("Method=findById, id={}", id);

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);

        return holidayMapper.toHolidayResponse(holiday);
    }

    @Override
    @Transactional
    public void update(final Long boardId, final Long holidayId, final HolidayRequest holidayRequest) {
        log.info("Method=update, boardId={}, holidayId={}, holidayRequest={}", boardId, holidayId, holidayRequest);

        Board board = boardService.findById(boardId);
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(ResourceNotFound::new);

        if (!holiday.getBoard().equals(board)) {
            throw new ResourceNotFound();
        }

        holidayMapper.updateFromRequest(holiday, holidayRequest);

        holidayRepository.save(holiday);
    }

    @Override
    @Transactional
    public void createImported(final Long boardId) {
        log.info("Method=createImported, boardId={}", boardId);

        List<Holiday> holidaysByBoard = holidayRepository.findAllByBoardId(boardId);
        List<HolidayVO> allHolidaysVOInCity = findAllHolidaysInCity();
        List<Holiday> allHolidaysInCity = holidayMapper.fromVOS(allHolidaysVOInCity, boardId);

        if (holidaysByBoard.containsAll(allHolidaysInCity)) {
            throw new HolidaysAlreadyImported();
        }

        Set<Holiday> holidays = new HashSet<>(holidaysByBoard);
        holidays.addAll(allHolidaysInCity);

        try {
            holidayRepository.saveAll(holidays);
        } catch (DataIntegrityViolationException e) {
            log.error("Method=createImported, e={}", e.getMessage(), e);
            throw new HolidaysAlreadyImported(e);
        }
    }

    private List<HolidayVO> findAllHolidaysInCity() {
        UserConfig userConfig = userService.findHolidayInfo();
        return holidayClient.findAllHolidaysInCity(LocalDate.now().getYear(),
                userConfig.getState(), userConfig.getCity(), userConfig.getHolidayToken());
    }
}
