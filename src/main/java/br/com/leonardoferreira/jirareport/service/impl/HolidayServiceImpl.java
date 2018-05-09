package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.HolidayRepository;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author s2it_leferreira
 * @since 5/7/18 6:52 PM
 */
@Slf4j
@Service
public class HolidayServiceImpl extends AbstractService implements HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayServiceImpl(final HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public List<Holiday> findAll() {
        log.info("Method=findAll");
        return (List<Holiday>) holidayRepository.findAll();
    }

    @Override
    public void create(Holiday holiday) {
        log.info("Method=create, holiday={}", holiday);
        holidayRepository.save(holiday);
    }

    @Override
    public void delete(final String id) {
        log.info("Method=delete, id={}", id);
        holidayRepository.deleteById(Objects.requireNonNull(DateUtil.displayFormat(id)));
    }

    @Override
    public Holiday findById(final String id) {
        log.info("Method=findById, id={}", id);
        return holidayRepository.findById(Objects.requireNonNull(DateUtil.displayFormat(id)))
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public void update(final Holiday holiday) {
        log.info("Method=update, holiday={}", holiday);
        holidayRepository.save(holiday);
    }
}
