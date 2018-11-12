package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.LeadTime;
import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.repository.LeadTimeRepository;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.service.LeadTimeConfigService;
import br.com.leonardoferreira.jirareport.service.LeadTimeService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lferreira on 17/05/18
 */
@Slf4j
@Service
public class LeadTimeServiceImpl extends AbstractService implements LeadTimeService {

    @Autowired
    private LeadTimeConfigService leadTimeConfigService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private LeadTimeRepository leadTimeRepository;

    @Autowired
    private BoardService boardService;

    @Override
    @Transactional
    @ExecutionTime
    public void createLeadTimes(final List<Issue> issues, final Long boardId) {
        log.info("Method=createLeadTimes, issues={}, boardId={}", issues, boardId);

        List<LeadTimeConfig> leadTimeConfigs = leadTimeConfigService.findAllByBoardId(boardId);
        List<LocalDate> holidays = holidayService.findDaysByBoard(boardId);
        Board board = boardService.findById(boardId);

        issues.forEach(issue -> {
            leadTimeRepository.deleteByIssueId(issue.getId());
            issue.setLeadTimes(leadTimeConfigs.stream()
                    .map(leadTimeConfig -> calcLeadTime(leadTimeConfig, issue, holidays, board.getIgnoreWeekend()))
                    .collect(Collectors.toSet()));
        });
    }

    private LeadTime calcLeadTime(final LeadTimeConfig leadTimeConfig, final Issue issue,
                                  final List<LocalDate> holidays, final Boolean ignoreWeekend) {
        log.info("Method=calcLeadTime, leadTimeConfig={}, issue={}, holidays={}", leadTimeConfig, issue, holidays);

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        Set<String> startColumns = leadTimeConfig.getStartColumns();
        Set<String> endColumns = leadTimeConfig.getEndColumns();

        for (Changelog cl : issue.getChangelog()) {
            if (startDate == null && startColumns.contains(cl.getTo())) {
                startDate = cl.getCreated();
            }

            if (endDate == null && endColumns.contains(cl.getTo())) {
                endDate = cl.getCreated();
            }
        }

        if ("BACKLOG".equals(leadTimeConfig.getStartColumn())) {
            startDate = issue.getCreated();
        }

        Long leadTime = 0L;
        if (startDate != null && endDate != null) {
            leadTime = DateUtil.daysDiff(startDate, endDate, holidays, ignoreWeekend);
        }

        return leadTimeRepository.save(LeadTime.builder()
                .leadTimeConfig(leadTimeConfig)
                .issue(issue)
                .leadTime(leadTime)
                .startDate(startDate)
                .endDate(endDate)
                .build());

    }
}
