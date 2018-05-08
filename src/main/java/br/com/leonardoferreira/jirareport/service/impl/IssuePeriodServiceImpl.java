package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.LeadTimeBySize;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartVO;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChartVO;
import br.com.leonardoferreira.jirareport.exception.CreateIssuePeriodException;
import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import br.com.leonardoferreira.jirareport.repository.IssueRepository;
import br.com.leonardoferreira.jirareport.repository.IssuePeriodRepository;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.service.IssuePeriodService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    private final IssueRepository issueRepository;

    public IssuePeriodServiceImpl(IssueService issueService, IssuePeriodRepository issuePeriodRepository,
                                  ChartService chartService, IssueRepository issueRepository) {
        this.issueService = issueService;
        this.issuePeriodRepository = issuePeriodRepository;
        this.chartService = chartService;
        this.issueRepository = issueRepository;
    }

    @Override
    public void create(IssueForm issueForm) throws CreateIssuePeriodException {
        log.info("Method=create, issueForm={}", issueForm);

        if (issuePeriodRepository.existsById(issueForm)) {
            log.error("Method=create, Msg=issuePeriod ja existente");
            throw new CreateIssuePeriodException("Registro já existente");
        }

        List<Issue> issues = issueService.findAllInJira(issueForm);
        issueRepository.saveAll(issues);
        Double avgLeadTime = issues.parallelStream()
                .filter(i -> i.getLeadTime() != null)
                .mapToLong(Issue::getLeadTime)
                .average().orElse(0D);

        CompletableFuture<ChartVO<Long, Long>> histogram = chartService.issueHistogram(issues);
        CompletableFuture<ChartVO<String, Long>> estimated = chartService.estimatedChart(issues);
        CompletableFuture<ChartVO<String, Double>> leadTimeBySystem = chartService.leadTimeBySystem(issues);
        CompletableFuture<ChartVO<String, Long>> tasksBySystem = chartService.tasksBySystem(issues);
        CompletableFuture<List<LeadTimeBySize>> leadTimeBySize = chartService.leadTimeBySize(issues);
        CompletableFuture<List<ColumnTimeAvg>> columnTimeAvg = chartService.columnTimeAvg(issues);

        try {
            IssuePeriod issuePeriod = new IssuePeriod(issueForm, issues, avgLeadTime,
                    histogram.get(), estimated.get(), leadTimeBySystem.get(),
                    tasksBySystem.get(), leadTimeBySize.get(), columnTimeAvg.get());
            issuePeriodRepository.save(issuePeriod);
        } catch (Exception e) {
            log.error("Method=create, Msg=erro ao gerar registro", e);
            throw new CreateIssuePeriodException(e.getMessage());
        }
    }

    @Override
    public List<IssuePeriod> findByProjectId(final Long projectId) {
        log.info("Method=findByProjectId, projectId={}", projectId);

        List<IssuePeriod> issues = issuePeriodRepository.findByProjectId(projectId);
        issues.sort(DateUtil::sort);

        return issues;
    }

    @Override
    public IssuePeriodChartVO getChartByIssues(List<IssuePeriod> issues) {
        log.info("Method=getChartByIssues, issues={}", issues);

        IssuePeriodChartVO issuePeriodChartVO = new IssuePeriodChartVO();
        issues.stream()
                .peek(issuePeriodChartVO::addLeadTime)
                .forEach(issuePeriodChartVO::addIssuesCount);

        return issuePeriodChartVO;
    }

    @Override
    public IssuePeriod findById(final IssueForm issueForm) {
        log.info("Method=findById, issueForm={}", issueForm);
        return issuePeriodRepository.findById(issueForm)
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public void remove(final IssueForm issueForm) {
        log.info("Method=remove, issueForm={}", issueForm);
        issuePeriodRepository.deleteById(issueForm);
    }

    @Override
    public void update(final IssueForm issueForm) throws CreateIssuePeriodException {
        log.info("Method=update, issueForm={}", issueForm);
        remove(issueForm);
        create(issueForm);
    }
}
