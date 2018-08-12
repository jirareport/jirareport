package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import br.com.leonardoferreira.jirareport.mapper.IssueMapper;
import br.com.leonardoferreira.jirareport.repository.IssueRepository;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.service.LeadTimeService;
import br.com.leonardoferreira.jirareport.util.CalcUtil;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import br.com.leonardoferreira.jirareport.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lferreira
 * @since 5/7/18 8:01 PM
 */
@Slf4j
@Service
public class IssueServiceImpl extends AbstractService implements IssueService {

    @Autowired
    private IssueClient issueClient;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ChartService chartService;

    @Autowired
    private LeadTimeService leadTimeService;

    @Override
    @ExecutionTime
    @Transactional
    public List<Issue> createByJql(final String jql, final Board board) {
        log.info("Method=createByJql, jql={}, board={}", jql, board);

        String issuesStr = issueClient.findAll(currentToken(), jql);

        List<Issue> issues = issueMapper.parse(issuesStr, board);

        issueRepository.saveAll(issues);
        leadTimeService.createLeadTimes(issues, board.getId());

        return issues;
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public SandBox findByExample(final Long boardId, final IssueForm issueForm) {
        log.info("Method=findByExample, boardId={}, issueForm={}", boardId, issueForm);

        if (issueForm.getStartDate() == null || issueForm.getEndDate() == null) {
            return new SandBox();
        }

        List<Issue> issues = issueRepository.findByExample(boardId, issueForm);
        final ChartAggregator chartAggregator = chartService.buildAllCharts(issues);

        Double avgLeadTime = issues.parallelStream()
                .filter(i -> i.getLeadTime() != null)
                .mapToLong(Issue::getLeadTime)
                .average().orElse(0D);

        return SandBox.builder()
                .issues(issues)
                .chartAggregator(chartAggregator)
                .avgLeadTime(avgLeadTime)
                .build();
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public SandBoxFilter findSandBoxFilters(final Long boardId, final SandBox sandBox, final IssueForm issueForm) {
        log.info("Method=findSandBoxFilters, boardId={}, sandBox={}, issueForm={}", boardId, sandBox, issueForm);

        return SandBoxFilter.builder()
                .estimatives(issueRepository.findAllEstimativesByBoardId(boardId))
                .keys(findAllKeys(sandBox, issueForm))
                .systems(issueRepository.findAllSystemsByBoardId(boardId))
                .epics(issueRepository.findAllEpicsByBoardId(boardId))
                .issueTypes(issueRepository.findAllIssueTypesByBoardId(boardId))
                .projects(issueRepository.findAllIssueProjectsByBoardId(boardId))
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public List<Issue> findByIssuePeriodId(final Long issuePeriodId) {
        log.info("Method=findByIssuePeriodId, issuePeriodId={}", issuePeriodId);

        return issueRepository.findByIssuePeriodId(issuePeriodId);
    }

    @Override
    @Transactional(readOnly = true)
    public String searchJQL(final IssuePeriodForm issuePeriodForm, final Board board) {
        log.info("Method=searchJQL, issuePeriodForm={}, board={}", issuePeriodForm, board);

        final List<String> fluxColumn = board.getFluxColumn();
        String lastColumn = fluxColumn == null || fluxColumn.isEmpty() ? "Done" : fluxColumn.get(fluxColumn.size() - 1);

        Map<String, Object> params = new HashMap<>();

        StringBuilder jql = new StringBuilder();
        jql.append(" project = {project} ");
        jql.append(" AND ( STATUS CHANGED TO {endColumn} DURING({startDate}, {endDate}) ");
        jql.append("       OR ( STATUS CHANGED TO {lastColumn} DURING ({startDate}, {endDate}) AND NOT STATUS CHANGED TO {endColumn} )");
        jql.append("     ) ");

        if (board.getIgnoreIssueType() != null && !board.getIgnoreIssueType().isEmpty()) {
            jql.append(" AND issueType NOT IN ({issueTypes}) ");
            params.put("issueTypes", board.getIgnoreIssueType());
        }

        jql.append(" AND status WAS IN ({startColumns}) ");
        jql.append(" AND status WAS IN ({endColumns}) ");

        params.put("project", board.getExternalId().toString());
        params.put("startDate", DateUtil.toENDate(issuePeriodForm.getStartDate()));
        params.put("endDate", DateUtil.toENDate(issuePeriodForm.getEndDate()) + " 23:59");
        params.put("lastColumn", lastColumn);
        params.put("endColumn", board.getEndColumn());
        params.put("endColumns", CalcUtil.calcEndColumns(board));
        params.put("startColumns", CalcUtil.calcStartColumns(board));

        return StringUtil.replaceParams(jql.toString(), params);
    }

    @Override
    @Transactional
    public void deleteAll(final List<Issue> issues) {
        log.info("Method=deleteAll, issues={}", issues);
        issueRepository.deleteAll(issues);
    }

    private List<String> findAllKeys(final SandBox sandBox, final IssueForm issueForm) {
        return Stream.concat(sandBox.getIssues().stream().map(Issue::getKey), issueForm.getKeys().stream())
                .distinct().sorted().collect(Collectors.toList());
    }

}
