package br.com.leonardoferreira.jirareport.repository.impl;

import br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime;
import br.com.leonardoferreira.jirareport.domain.DynamicFieldsValues;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.repository.IssueCustomRepository;
import br.com.leonardoferreira.jirareport.util.ParseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.ResultSetMetaData;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
public class IssueCustomRepositoryImpl implements IssueCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    @ExecutionTime
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Issue> findByExample(final Long boardId, final IssueForm issueForm) {
        log.info("Method=findByExample, boardId={}, issueForm={}", boardId, issueForm);

        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT * FROM issue ");
        sb.append(" left join lead_time on issue.id = lead_time.issue_id ");
        sb.append(" left join lead_time_config on lead_time.lead_time_config_id = lead_time_config.id ");
        sb.append(" WHERE issue.board_id = :boardId ");

        sb.append(" AND issue.end_date BETWEEN :startDate AND :endDate ");

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
            sb.append(" AND issue.issue_type IN (:issueTypes) ");
            params.put("issueTypes", issueForm.getIssueTypes());
        }

        if (!CollectionUtils.isEmpty(issueForm.getProjects())) {
            sb.append(" AND issue.project IN (:projects) ");
            params.put("projects", issueForm.getProjects());
        }

        if (!CollectionUtils.isEmpty(issueForm.getPriorities())) {
            sb.append(" AND issue.priority IN (:priorities) ");
            params.put("priorities", issueForm.getPriorities());
        }

        if (!CollectionUtils.isEmpty(issueForm.getDynamicFieldsValues())) {
            for (int i = 0; i < issueForm.getDynamicFieldsValues().size(); i++) {
                DynamicFieldsValues dynamicFieldsValue = issueForm.getDynamicFieldsValues().get(i);
                if (StringUtils.isEmpty(dynamicFieldsValue.getField()) || CollectionUtils.isEmpty(dynamicFieldsValue.getValues())) {
                    continue;
                }

                String field = (dynamicFieldsValue.getField() + i).replaceAll(" ", "");
                sb.append(" AND issue.dynamic_fields->>'").append(dynamicFieldsValue.getField())
                        .append("' in (:").append(field).append(") ");
                params.put(field, dynamicFieldsValue.getValues());
            }
        }

        params.put("boardId", boardId);
        params.put("startDate", issueForm.getStartDate().atStartOfDay());
        params.put("endDate", issueForm.getEndDate().atTime(LocalTime.of(23, 59, 59)));

        sb.append(" ORDER BY issue.key ");

        Query nativeQuery = entityManager.createNativeQuery(sb.toString(), Issue.class);
        params.forEach(nativeQuery::setParameter);
        return nativeQuery.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DynamicFieldsValues> findAllDynamicFieldValues(final Long boardId) {
        log.info("Method=findAllDynamicFieldValues, boardId={}", boardId);

        List<String> dynamicFields = findAllDynamicFieldsByBoardId(boardId);

        StringBuilder query = new StringBuilder("select ");
        dynamicFields.forEach(it -> query.append("array_to_json(array_remove(array_agg(distinct fields.\"")
                .append(it).append("\"), null)) as \"").append(it).append("\","));
        query.deleteCharAt(query.length() - 1);
        query.append(" from issue, jsonb_to_record(dynamic_fields) as fields( ");
        dynamicFields.forEach(it -> query.append("\"").append(it).append("\" text,"));
        query.deleteCharAt(query.length() - 1);
        query.append(")");
        query.append(" where board_id = ? ");

        return jdbcTemplate.queryForObject(query.toString(), (rs, rowNum) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            List<DynamicFieldsValues> dynamicFieldsValues = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnLabel = metaData.getColumnLabel(i);
                List<String> values = ParseUtil.toStringArray(objectMapper, rs.getString(columnLabel));

                dynamicFieldsValues.add(new DynamicFieldsValues(columnLabel, values));
            }
            return dynamicFieldsValues;
        }, boardId);
    }

    private List<String> findAllDynamicFieldsByBoardId(final Long boardId) {
        log.info("Method=findAllDynamicFieldsByBoardId, boardId={}", boardId);

        String query = "select distinct jsonb_object_keys(dynamic_fields) from issue where board_id = ?";
        return jdbcTemplate.queryForList(query, String.class, boardId);
    }

}
