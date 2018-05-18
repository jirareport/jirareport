package br.com.leonardoferreira.jirareport.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.LeadTime;
import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import br.com.leonardoferreira.jirareport.domain.embedded.Changelog;
import br.com.leonardoferreira.jirareport.mapper.IssueMapper;
import br.com.leonardoferreira.jirareport.repository.LeadTimeRepository;
import br.com.leonardoferreira.jirareport.service.HolidayService;
import br.com.leonardoferreira.jirareport.service.LeadTimeConfigService;
import br.com.leonardoferreira.jirareport.service.LeadTimeService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lferreira on 17/05/18
 */
@Slf4j
@Service
public class LeadTimeServiceImpl implements LeadTimeService {

    @Autowired
    private LeadTimeConfigService leadTimeConfigService;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private LeadTimeRepository leadTimeRepository;

    @Override
    @Transactional
    public void createLeadTimes(final List<Issue> issues, final Long projectId) {
        List<LeadTimeConfig> leadTimeConfigs = leadTimeConfigService.findAllByProjectId(projectId);
        final List<String> holidays = holidayService.findByProject(projectId)
                .stream().map(Holiday::getEnDate).collect(Collectors.toList());

        issues.forEach(issue -> {
            issue.setLeadTimes(leadTimeConfigs.stream()
                    .map(leadTimeConfig ->
                            calcLeadTime(leadTimeConfig, issue, holidays)
                    ).collect(Collectors.toList()));
        });
    }

    @Transactional
    private LeadTime calcLeadTime(final LeadTimeConfig leadTimeConfig,
                                  final Issue issue,
                                  final List<String> holidays) {

        String startDate = null;
        String endDate = null;

        for (Changelog cl : issue.getChangelog()) {
            if (startDate == null && leadTimeConfig.getStartColumn().equals(cl.getTo())) {
                startDate = DateUtil.toENDate(cl.getCreated());
            }

            if (endDate == null && leadTimeConfig.getEndColumn().equals(cl.getTo())) {
                endDate = DateUtil.toENDate(cl.getCreated());
            }
        }


        Long leadTimeN = 0L;
        if (startDate != null && endDate != null) {
            leadTimeN = issueMapper.daysDiff(startDate, endDate, holidays);
        }

        LeadTime leadTime = new LeadTime();
        leadTime.setLeadTimeConfig(leadTimeConfig);
        leadTime.setIssue(issue);
        leadTime.setLeadTime(leadTimeN);
        leadTime.setStartDate(startDate);
        leadTime.setEndDate(endDate);

        leadTimeRepository.save(leadTime);

        return leadTime;
    }
}
