package br.com.leonardoferreira.jirareport.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.repository.IssueCustomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class IssueCustomRepositoryImpl implements IssueCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Issue> findByExample(final Long projectId, final IssueForm issueForm) {
        log.info("Method=findByExample, projectId={}, issueForm={}", projectId, issueForm);
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append(" select issue.* from issue ");
        sb.append(" inner join issue_period_issue on issue_period_issue.issue_key = issue.key ");
        sb.append(" where issue_period_issue.project_id = :projectId");

        sb.append(" AND to_date(issue.end_date, 'DD/MM/YYYY') BETWEEN :startDate and :endDate ");

        if (issueForm.getKeys() != null && !issueForm.getKeys().isEmpty()) {
            sb.append(" AND issue.key in (:keys) ");
            params.put("keys", issueForm.getKeys());
        }

        if (issueForm.getTaskSize() != null && !issueForm.getTaskSize().isEmpty()) {
            sb.append(" AND issue.estimated in (:taskSize) ");
            params.put("taskSize", issueForm.getTaskSize());
        }

        if (issueForm.getSystems() != null && !issueForm.getSystems().isEmpty()) {
            sb.append(" AND issue.components <@ :systems\\:\\:jsonb ");
            params.put("systems", new ObjectMapper().writeValueAsString(issueForm.getSystems()));
        }

        params.put("projectId", projectId);
        params.put("startDate", issueForm.getStartDate());
        params.put("endDate", issueForm.getEndDate());

        Query nativeQuery = entityManager.createNativeQuery(sb.toString(), Issue.class);
        params.forEach(nativeQuery::setParameter);

        return nativeQuery.getResultList();
    }

}
