package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Project;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.HolidayRepository;
import br.com.leonardoferreira.jirareport.repository.ProjectRepository;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author s2it_leferreira
 * @since 5/7/18 6:52 PM
 */
@Slf4j
@Service
public class HolidayServiceImpl extends AbstractService implements HolidayService {

    @Autowired
    private final HolidayRepository holidayRepository;

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final HolidayClient holidayClient;

    @Autowired
    private final GeoNamesClient geoNamesClient;

    @Override
    @Transactional(readOnly = true)
    public Page<Holiday> findByProject(final Long projectId, final Pageable pageable) {
        log.info("Method=findByProject, projectId={}", projectId);
        return holidayRepository.findAllByProjectId(projectId, pageable);
    }

    @Override
    @Transactional
    public void create(final Long projectId, final Holiday holiday) {
        log.info("Method=create, holiday={}", holiday);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ResourceNotFound::new);
        holiday.setProject(project);
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
    public List<HolidayVO> findAllHolidaysInCity(final String year, final String state, final String city) {
        return holidayClient.findAllHolidaysInCity(year, state, city);
    }

    @Override
    @Transactional
    public Boolean createImported(final Long projectId, final String city) {

        String[] split = city.split("-");
        String state = split[1];
        String cityRuled = StringUtil.applyRulesForHolidaysService(split[0]);

        List<Holiday> holidaysByProject = findByProject(projectId);
        Set<String> holidayAlreadyRegistered = holidaysByProject.stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());

        List<HolidayVO> allHolidaysInCity = findAllHolidaysInCity(new Integer(Calendar.getInstance().get(Calendar.YEAR)).toString(), state, cityRuled);
        List<HolidayVO> onlyNewHolidays = allHolidaysInCity.stream()
                .filter(e -> !holidayAlreadyRegistered.contains(e.getDate()))
                .collect(Collectors.toList());

        if (onlyNewHolidays.isEmpty()) {
            return false;
        } else {
            onlyNewHolidays.forEach(holidayVO -> create(projectId,
                    Holiday.builder().date(holidayVO.getDate()).description(holidayVO.getName()).build()));
            return true;
        }
    }

    @Override
    public GeoNamesWrapperVO findAllStatesOfBrazil() {
        return geoNamesClient.findAllChildrenByGeonameId("3469034");
    }

    @Override
    public GeoNamesWrapperVO findAllCitiesByState(String geonameIdState) {
        return geoNamesClient.findAllChildrenByGeonameId(geonameIdState);
    }
}
