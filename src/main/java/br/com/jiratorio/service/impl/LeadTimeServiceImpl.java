package br.com.jiratorio.service.impl;

import br.com.jiratorio.aspect.annotation.ExecutionTime;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.Issue;
import br.com.jiratorio.domain.LeadTime;
import br.com.jiratorio.domain.LeadTimeConfig;
import br.com.jiratorio.domain.embedded.Changelog;
import br.com.jiratorio.repository.LeadTimeRepository;
import br.com.jiratorio.service.BoardService;
import br.com.jiratorio.service.HolidayService;
import br.com.jiratorio.service.LeadTimeConfigService;
import br.com.jiratorio.service.LeadTimeService;
import br.com.jiratorio.util.CalcUtil;
import br.com.jiratorio.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LeadTimeServiceImpl extends AbstractService implements LeadTimeService {

    private final LeadTimeConfigService leadTimeConfigService;

    private final HolidayService holidayService;

    private final LeadTimeRepository leadTimeRepository;

    private final BoardService boardService;

    public LeadTimeServiceImpl(final LeadTimeConfigService leadTimeConfigService,
                               final HolidayService holidayService,
                               final LeadTimeRepository leadTimeRepository,
                               final BoardService boardService) {
        this.leadTimeConfigService = leadTimeConfigService;
        this.holidayService = holidayService;
        this.leadTimeRepository = leadTimeRepository;
        this.boardService = boardService;
    }

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
                    .map(leadTimeConfig -> calcLeadTime(leadTimeConfig, issue, holidays, board))
                    .collect(Collectors.toSet()));
        });
    }

    private LeadTime calcLeadTime(final LeadTimeConfig leadTimeConfig, final Issue issue,
                                  final List<LocalDate> holidays, final Board board) {
        log.info("Method=calcLeadTime, leadTimeConfig={}, issue={}, holidays={}", leadTimeConfig, issue, holidays);

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        Set<String> startColumns = CalcUtil.calcStartColumns(leadTimeConfig.getStartColumn(), leadTimeConfig.getEndColumn(), board.getFluxColumn());
        Set<String> endColumns = CalcUtil.calcEndColumns(leadTimeConfig.getEndColumn(), board.getFluxColumn());

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
            leadTime = DateUtil.daysDiff(startDate, endDate, holidays, board.getIgnoreWeekend());
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
