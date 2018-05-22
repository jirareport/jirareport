package br.com.leonardoferreira.jirareport.repository.impl;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.repository.IssueCustomRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Repository
public class IssueCustomRepositoryImpl implements IssueCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    @SneakyThrows
    @ExecutionTime
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Issue> findByExample(final Long projectId, final IssueForm issueForm) {
        log.info("Method=findByExample, projectId={}, issueForm={}", projectId, issueForm);
        
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT DISTINCT issue FROM Issue issue ");
        sb.append(" JOIN FETCH issue.leadTimes leadTimes ");
        sb.append(" JOIN FETCH leadTimes.leadTimeConfig ");
        sb.append(" JOIN issue.issuePeriods issuePeriods ");
        sb.append(" WHERE issuePeriods.id.projectId = :projectId ");

        sb.append(" AND TO_DATE(issue.endDate, 'DD/MM/YYYY') BETWEEN :startDate AND :endDate ");

        if (!CollectionUtils.isEmpty(issueForm.getKeys())) {
            sb.append(" AND issue.key NOT IN (:keys) ");
            params.put("keys", issueForm.getKeys());
        }

        if (!CollectionUtils.isEmpty(issueForm.getTaskSize())) {
            sb.append(" AND issue.estimated IN (:taskSize) ");
            params.put("taskSize", issueForm.getTaskSize());
        }

        if (!CollectionUtils.isEmpty(issueForm.getSystems())) {
            sb.append(" AND issue.system IN (:systems) ");
            params.put("systems", issueForm.getSystems());
        }

        if (!CollectionUtils.isEmpty(issueForm.getEpics())) {
            sb.append(" AND issue.epic IN (:epics) ");
            params.put("epics", issueForm.getEpics());
        }

        if (!CollectionUtils.isEmpty(issueForm.getIssueTypes())) {
            sb.append(" AND issue.issueType IN (:issueTypes) ");
            params.put("issueTypes", issueForm.getIssueTypes());
        }

        if (!CollectionUtils.isEmpty(issueForm.getProjects())) {
            sb.append(" AND issue.project IN (:projects) ");
            params.put("projects", issueForm.getProjects());
        }

        params.put("projectId", projectId);
        params.put("startDate", issueForm.getStartDate());
        params.put("endDate", issueForm.getEndDate());

        sb.append(" ORDER BY issue.key ");

        Query nativeQuery = entityManager.createQuery(sb.toString(), Issue.class);
        params.forEach(nativeQuery::setParameter);

        return nativeQuery.getResultList();
    }

}
