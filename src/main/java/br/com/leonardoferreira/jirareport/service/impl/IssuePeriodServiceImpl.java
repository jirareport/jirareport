package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChart;
import br.com.leonardoferreira.jirareport.exception.CreateIssuePeriodException;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.IssuePeriodRepository;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.service.IssuePeriodService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lferreira
 * @since 7/28/17 12:50 PM
 */
@Slf4j
@Service
public class IssuePeriodServiceImpl extends AbstractService implements IssuePeriodService {

    private final IssueService issueService;

    private final IssuePeriodRepository issuePeriodRepository;

    private final ChartService chartService;

    public IssuePeriodServiceImpl(IssueService issueService, IssuePeriodRepository issuePeriodRepository,
                                  ChartService chartService) {
        this.issueService = issueService;
        this.issuePeriodRepository = issuePeriodRepository;
        this.chartService = chartService;
    }

    @Override
    public void create(IssuePeriodId issuePeriodId) throws CreateIssuePeriodException {
        log.info("Method=create, issuePeriodId={}", issuePeriodId);

        if (issuePeriodRepository.existsById(issuePeriodId)) {
            log.error("Method=create, Msg=issuePeriod ja existente");
            throw new CreateIssuePeriodException("Registro já existente");
        }

        List<Issue> issues = issueService.findAllInJira(issuePeriodId);
        issueService.saveAll(issues);
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
            throw new CreateIssuePeriodException(e.getMessage());
        }
    }

    @Override
    public List<IssuePeriod> findByProjectId(final Long projectId) {
        log.info("Method=findByProjectId, projectId={}", projectId);

        List<IssuePeriod> issues = issuePeriodRepository.findByIdProjectId(projectId);
        issues.sort(DateUtil::sort);

        return issues;
    }

    @Override
    public IssuePeriodChart getChartByIssues(List<IssuePeriod> issues) {
        log.info("Method=getChartByIssues, issues={}", issues);

        IssuePeriodChart issuePeriodChart = new IssuePeriodChart();
        issues.stream()
                .peek(issuePeriodChart::addLeadTime)
                .forEach(issuePeriodChart::addIssuesCount);

        return issuePeriodChart;
    }

    @Override
    public IssuePeriod findById(final IssuePeriodId issuePeriodId) {
        log.info("Method=findById, issuePeriodId={}", issuePeriodId);
        return issuePeriodRepository.findById(issuePeriodId)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public void remove(final IssuePeriodId issuePeriodId) {
        log.info("Method=remove, issuePeriodId={}", issuePeriodId);
        issuePeriodRepository.deleteById(issuePeriodId);
    }

    @Override
    public void update(final IssuePeriodId issuePeriodId) throws CreateIssuePeriodException {
        log.info("Method=update, issuePeriodId={}", issuePeriodId);
        remove(issuePeriodId);
        create(issuePeriodId);
    }
}
