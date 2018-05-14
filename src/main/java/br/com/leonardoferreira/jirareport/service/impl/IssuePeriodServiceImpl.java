package br.com.leonardoferreira.jirareport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.IssueCountBySize;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChart;
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
import org.springframework.util.StringUtils;

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
            throw new CreateIssuePeriodException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssuePeriod> findByProjectId(final Long projectId) {
        log.info("Method=findByProjectId, projectId={}", projectId);

        List<IssuePeriod> issues = issuePeriodRepository.findByIdProjectId(projectId);
        issues.sort(DateUtil::sort);

        return issues;
    }

    @Override
    @Transactional
    public IssuePeriodChart getChartByIssues(final List<IssuePeriod> issuePeriods) {
        log.info("Method=getChartByIssues, issuePeriods={}", issuePeriods);

        IssuePeriodChart issuePeriodChart = new IssuePeriodChart();
        issuePeriods.stream()
                .peek(issuePeriodChart::addLeadTime)
                .forEach(issuePeriodChart::addIssuesCount);

        IssueCountBySize issueCountBySize = buildIssueCountBySize(issuePeriods);

        issuePeriodChart.setIssueCountBySize(issueCountBySize);

        return issuePeriodChart;
    }

    @Override
    @Transactional
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

    private IssueCountBySize buildIssueCountBySize(final List<IssuePeriod> issuePeriods) {
        long start = System.currentTimeMillis();

        List<String> sizes = new ArrayList<>();
        Map<String, Map<String, Long>> idEstimative = new HashMap<>();
        for (IssuePeriod issuePeriod : issuePeriods) {
            Map<String, Long> collect = issuePeriod.getIssues().stream()
                    .filter(i -> !StringUtils.isEmpty(i.getEstimated()))
                    .peek(i -> sizes.add(i.getEstimated()))
                    .collect(Collectors.groupingBy(Issue::getEstimated, Collectors.counting()));
            idEstimative.put(issuePeriod.getId().getDates(), collect);
        }

        idEstimative.forEach((k, v) -> {
            for (String size : sizes) {
                if (!v.containsKey(size)) {
                    v.put(size, 0L);
                }
            }
        });

        Map<String, List<Long>> datasources = new HashMap<>();
        for (Map<String, Long> stringLongMap : idEstimative.values()) {
            stringLongMap.forEach((k, v) -> {
                if (datasources.containsKey(k)) {
                    List<Long> longs = datasources.get(k);
                    longs.add(v);
                    datasources.put(k, longs);
                } else {
                    ArrayList<Long> value = new ArrayList<>();
                    value.add(v);
                    datasources.put(k, value);
                }
            });
        }

        IssueCountBySize issueCountBySize = new IssueCountBySize();
        issueCountBySize.setLabels(idEstimative.keySet());
        issueCountBySize.setDatasources(datasources);

        long end = System.currentTimeMillis();
        log.info("Method=buildIssueCountBySize, ms={}", end - start);
        return issueCountBySize;
    }
}
