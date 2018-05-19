package br.com.leonardoferreira.jirareport.service.impl;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.IssueCountBySize;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChart;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodList;
import br.com.leonardoferreira.jirareport.domain.vo.LeadTimeCompareChart;
import br.com.leonardoferreira.jirareport.exception.CreateIssuePeriodException;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.IssuePeriodRepository;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.service.IssuePeriodService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lferreira
 * @since 7/28/17 12:50 PM
 */
@Slf4j
@Service
public class IssuePeriodServiceImpl extends AbstractService implements IssuePeriodService {

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssuePeriodRepository issuePeriodRepository;

    @Autowired
    private ChartService chartService;

    @Override
    @Transactional
    public void create(final IssuePeriodId issuePeriodId) throws CreateIssuePeriodException {
        log.info("Method=create, issuePeriodId={}", issuePeriodId);

        if (issuePeriodRepository.existsById(issuePeriodId)) {
            log.error("Method=create, Msg=issuePeriod ja existente");
            throw new CreateIssuePeriodException("Registro já existente");
        }

        List<Issue> issues = issueService.findAllInJira(issuePeriodId);

        Double avgLeadTime = issues.parallelStream()
                .filter(i -> i.getLeadTime() != null)
                .mapToLong(Issue::getLeadTime)
                .average().orElse(0D);

        final ChartAggregator chartAggregator = chartService.buildAllCharts(issues);

        try {
            IssuePeriod issuePeriod = new IssuePeriod(issuePeriodId, issues, avgLeadTime, chartAggregator);
            issuePeriodRepository.save(issuePeriod);
        } catch (Exception e) {
            log.error("Method=create, Msg=erro ao gerar registro", e);
            throw new CreateIssuePeriodException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssuePeriod> findByProjectId(final Long projectId) {
        log.info("Method=findByProjectId, projectId={}", projectId);

        List<IssuePeriod> issuePeriods = issuePeriodRepository.findByIdProjectId(projectId);
        issuePeriods.sort(DateUtil::sort);

        return issuePeriods;
    }

    @Override
    @Transactional(readOnly = true)
    public IssuePeriodChart buildCharts(final List<IssuePeriod> issuePeriods) {
        log.info("Method=buildCharts, issuePeriods={}", issuePeriods);

        IssuePeriodChart issuePeriodChart = new IssuePeriodChart();
        issuePeriods.stream()
                .peek(issuePeriodChart::addLeadTime)
                .forEach(issuePeriodChart::addIssuesCount);

        IssueCountBySize issueCountBySize = chartService.buildIssueCountBySize(issuePeriods);
        issuePeriodChart.setIssueCountBySize(issueCountBySize);

        LeadTimeCompareChart<Double> leadTimeCompareChart = chartService.calcLeadTimeCompareByPeriod(issuePeriods);
        issuePeriodChart.setLeadTimeCompareChart(leadTimeCompareChart);

        return issuePeriodChart;
    }

    @Override
    @Transactional(readOnly = true)
    public IssuePeriod findById(final IssuePeriodId issuePeriodId) {
        log.info("Method=findById, issuePeriodId={}", issuePeriodId);

        return issuePeriodRepository.findById(issuePeriodId)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    @Transactional
    public void remove(final IssuePeriodId issuePeriodId) {
        log.info("Method=remove, issuePeriodId={}", issuePeriodId);

        issuePeriodRepository.deleteById(issuePeriodId);
    }

    @Override
    @Transactional(rollbackFor = CreateIssuePeriodException.class)
    public void update(final IssuePeriodId issuePeriodId) throws CreateIssuePeriodException {
        log.info("Method=update, issuePeriodId={}", issuePeriodId);

        remove(issuePeriodId);
        create(issuePeriodId);
    }

    @Override
    @Transactional(readOnly = true)
    public IssuePeriodList findIssuePeriodsAndCharts(final Long projectId) {
        List<IssuePeriod> issuePeriods = findByProjectId(projectId);
        IssuePeriodChart issuePeriodChart = buildCharts(issuePeriods);

        return IssuePeriodList.builder()
                .issuePeriods(issuePeriods)
                .issuePeriodChart(issuePeriodChart)
                .build();
    }
}
