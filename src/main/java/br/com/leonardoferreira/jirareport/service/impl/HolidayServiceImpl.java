package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.HolidayClient;
import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.HolidayRepository;
import br.com.leonardoferreira.jirareport.repository.ProjectRepository;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import java.util.List;
import java.util.Optional;
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
    private final ProjectRepository projectRepository;
    private final HolidayClient holidayClient;

    public HolidayServiceImpl(final HolidayRepository holidayRepository,
                              final ProjectRepository projectRepository, HolidayClient holidayClient) {
        this.holidayRepository = holidayRepository;
        this.projectRepository = projectRepository;
        this.holidayClient = holidayClient;
    }

    @Override
    public List<Holiday> findByProject(Long projectId) {
        log.info("Method=findByProject");
        return (List<Holiday>) holidayRepository.findByProjectId(projectId);
    }


    @Override
    public void create(Long projectId, Holiday holiday) {
        log.info("Method=create, holiday={}", holiday);
        final Optional<Project> project = projectRepository.findById(projectId);
        holiday.setProject(project.orElseThrow(()-> new IllegalArgumentException("Projeto obrigatorio")));
        holidayRepository.save(holiday);
    }

    @Override
    public void delete(final Long id) {
        log.info("Method=delete, id={}", id);
        holidayRepository.deleteById(id);
    }

    @Override
    public Holiday findById(final Long id) {
        log.info("Method=findById, id={}", id);
        return holidayRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public void update(Long projectId, final Holiday holiday) {
        log.info("Method=update, holiday={}", holiday);
        final Optional<Project> project = projectRepository.findById(projectId);
        holiday.setProject(project.orElseThrow(()-> new IllegalArgumentException("Projeto obrigatorio")));
        holidayRepository.save(holiday);
    }

    @Override
    public List<HolidayVO> findAllHolidaysInCity(String year, String state, String city) {
        return holidayClient.findAllHolidaysInCity(year, state, city);
    }
}
