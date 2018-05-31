package br.com.leonardoferreira.jirareport.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.client.IssueClient;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.ChartAggregator;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import br.com.leonardoferreira.jirareport.mapper.IssueMapper;
import br.com.leonardoferreira.jirareport.repository.IssueRepository;
import br.com.leonardoferreira.jirareport.service.BoardService;
import br.com.leonardoferreira.jirareport.service.ChartService;
import br.com.leonardoferreira.jirareport.service.IssueService;
import br.com.leonardoferreira.jirareport.service.LeadTimeService;
import br.com.leonardoferreira.jirareport.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private BoardService boardService;

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
    public List<Issue> findAllInJira(final IssuePeriodId issuePeriodId) {
        log.info("Method=findAllInJira, issuePeriodId={}", issuePeriodId);

        final Board board = boardService.findById(issuePeriodId.getBoardId());

        String issuesStr = issueClient.findAll(currentToken(), buildJQL(issuePeriodId, board));

        List<Issue> issues = issueMapper.parse(issuesStr, board);
        List<String> keys = issues.stream().map(Issue::getKey).collect(Collectors.toList());

        leadTimeService.deleteByIssueKeys(keys);
        issueRepository.deleteByKeysAndBoardId(keys, issuePeriodId.getBoardId());

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
    public List<Issue> findByIssuePeriodId(final IssuePeriodId issuePeriodId) {
        log.info("Method=findByIssuePeriodId, issuePeriodId={}", issuePeriodId);

        return issueRepository.findByIssuePeriodId(issuePeriodId.getBoardId(), issuePeriodId.getStartDate(), issuePeriodId.getEndDate());
    }

    private String buildJQL(final IssuePeriodId issuePeriodId, final Board board) {
        log.info("Method=buildJQL, issuePeriodId={}, board={}", issuePeriodId, board);

        StringBuilder jql = new StringBuilder();
        jql.append("project = ").append(board.getExternalId()).append(" ");
        if (!CollectionUtils.isEmpty(board.getIgnoreIssueType())) {
            jql.append(" AND issuetype not in (");
            jql.append(String.join(",", board.getIgnoreIssueType()
                    .stream()
                    .map(i -> "'" + i + "'")
                    .collect(Collectors.toList()))).append(" ) ");
        }
        jql.append("AND (STATUS CHANGED TO '").append(board.getEndColumn()).append("' DURING('");
        jql.append(DateUtil.toENDate(issuePeriodId.getStartDate())).append("', '");
        jql.append(DateUtil.toENDate(issuePeriodId.getEndDate())).append(" 23:59')");
        jql.append("OR ( Resolved >= ");
        jql.append(DateUtil.toENDate(issuePeriodId.getStartDate()));
        jql.append(" AND Resolved <= '");
        jql.append(DateUtil.toENDate(issuePeriodId.getEndDate())).append(" 23:59'");
        jql.append(" AND NOT STATUS CHANGED TO '").append(board.getEndColumn()).append("' ");
        jql.append("   )");
        jql.append(")");

        return jql.toString();
    }

    private List<String> findAllKeys(final SandBox sandBox, final IssueForm issueForm) {
        return Stream.concat(sandBox.getIssues().stream().map(Issue::getKey), issueForm.getKeys().stream())
                .distinct().sorted().collect(Collectors.toList());
    }

}
