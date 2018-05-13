package br.com.leonardoferreira.jirareport.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.com.leonardoferreira.jirareport.client.HolidayClient;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.mapper.HolidayMapper;
import br.com.leonardoferreira.jirareport.repository.HolidayRepository;
import br.com.leonardoferreira.jirareport.repository.ProjectRepository;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author s2it_leferreira
 * @since 5/7/18 6:52 PM
 */
@Slf4j
@Service
public class HolidayServiceImpl extends AbstractService implements HolidayService {

    private final HolidayRepository holidayRepository;
    private final ProjectRepository projectRepository;
    private final HolidayClient holidayClient;
    private final HolidayMapper holidayMapper;

    public HolidayServiceImpl(final HolidayRepository holidayRepository,
                              final ProjectRepository projectRepository,
                              final HolidayClient holidayClient,
                              final HolidayMapper holidayMapper) {
        this.holidayRepository = holidayRepository;
        this.projectRepository = projectRepository;
        this.holidayClient = holidayClient;
        this.holidayMapper = holidayMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Holiday> findByProject(final Long projectId) {
        log.info("Method=findByProject");
        return holidayRepository.findByProjectId(projectId);
    }


    @Override
    @Transactional
    public void create(final Long projectId, final Holiday holiday) {
        log.info("Method=create, holiday={}", holiday);

        final Optional<Project> project = projectRepository.findById(projectId);
        holiday.setProject(project.orElseThrow(() -> new IllegalArgumentException("Projeto obrigatorio")));
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
    public void update(final Long projectId, final Holiday holiday) {
        log.info("Method=update, holiday={}", holiday);

        final Project project = projectRepository.findById(projectId)
                .orElseThrow(ResourceNotFound::new);

        holiday.setProject(project);
        holidayRepository.save(holiday);
    }

    @Override
    @Transactional
    public boolean createImported(final Long projectId) {
        log.info("Method=createImported, projectId={}", projectId);

        List<Holiday> holidaysByProject = findByProject(projectId);
        List<HolidayVO> allHolidaysVOInCity = holidayClient.findAllHolidaysInCity("2018", "SP", "ARARAQUARA");
        List<Holiday> allHolidaysInCity = holidayMapper.fromVOS(allHolidaysVOInCity, projectId);

        if (holidaysByProject.containsAll(allHolidaysInCity)) {
            return false;
        }

        Set<Holiday> holidays = new HashSet<>(holidaysByProject);
        holidays.addAll(allHolidaysInCity);

        try {
            holidayRepository.saveAll(holidays);
        } catch (DataIntegrityViolationException e) {
            log.error("Method=createImported, e={}", e.getMessage(), e);
            return false;
        }

        return true;
    }
}
