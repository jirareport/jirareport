package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssueResult;
import br.com.leonardoferreira.jirareport.domain.LeadtimePrediction;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartVO;
import br.com.leonardoferreira.jirareport.domain.vo.IssueResultChartVO;
import br.com.leonardoferreira.jirareport.exception.CreateIssueResultException;
import br.com.leonardoferreira.jirareport.repository.IssueResultRepository;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author leferreira
 * @since 7/28/17 12:50 PM
 */
@Slf4j
@Service
public class IssueResultService extends AbstractService {

    @Autowired
    private IssueClient issueClient;

    @Autowired
    private IssueResultRepository issueResultRepository;

    @Autowired
    private ChartService chartService;

    public void create(IssueForm issueForm) throws CreateIssueResultException {
        log.info("Method=create, issueForm={}", issueForm);

        if (issueResultRepository.exists(issueForm)) {
            log.error("Method=create, Msg=issueResult ja existente");
            throw new CreateIssueResultException("Registro já existente");
        }

        List<Issue> issues = issueClient.findAll(currentToken(), issueForm);
        Double avgLeadTime = issues.parallelStream()
                .filter(i -> i.getLeadTime() != null)
                .mapToLong(Issue::getLeadTime)
                .average().orElse(0D);

        CompletableFuture<ChartVO<Long, Long>> histogram = chartService.issueHistogram(issues);
        CompletableFuture<ChartVO<String, Long>> estimated = chartService.estimatedChart(issues);
        CompletableFuture<ChartVO<String, Double>> leadTimeBySystem = chartService.leadTimeBySystem(issues);
        CompletableFuture<ChartVO<String, Long>> tasksBySystem = chartService.tasksBySystem(issues);
        CompletableFuture<List<LeadtimePrediction>> prediction = chartService.predictionChart(issues);

        try {
            IssueResult issueResult = new IssueResult(issueForm, issues, avgLeadTime,
                    histogram.get(), estimated.get(), leadTimeBySystem.get(), tasksBySystem.get(), prediction.get());
            issueResultRepository.save(issueResult);
        } catch (Exception e) {
            log.error("Method=create, Msg=erro ao gerar registro", e);
            throw new CreateIssueResultException(e.getMessage());
        }
    }

    public List<IssueResult> findByProjectId(final Long projectId) {
        log.info("Method=findByProjectId, projectId={}", projectId);

        List<IssueResult> issues = issueResultRepository.findByProjectId(projectId);
        issues.sort(DateUtil::sort);

        return issues;
    }

    public IssueResultChartVO getChartByIssues(List<IssueResult> issues) {
        log.info("Method=getChartByIssues, issues={}", issues);

        IssueResultChartVO issueResultChartVO = new IssueResultChartVO();
        issues.stream()
                .peek(issueResultChartVO::addLeadTime)
                .forEach(issueResultChartVO::addIssuesCount);

        return issueResultChartVO;
    }

    public IssueResult findById(final IssueForm issueForm) {
        log.info("Method=findById, issueForm={}", issueForm);
        return issueResultRepository.findOne(issueForm);
    }

    public void remove(final IssueForm issueForm) {
        log.info("Method=remove, issueForm={}", issueForm);
        issueResultRepository.delete(issueForm);
    }
}
