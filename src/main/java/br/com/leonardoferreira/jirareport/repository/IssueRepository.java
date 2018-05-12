package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Issue;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IssueRepository extends CrudRepository<Issue, String>, IssueCustomRepository {

    @Query(value = "select distinct issue.estimated from issue"
            + " inner join issue_period_issue on issue_period_issue.issue_key = issue.key "
            + " where issue_period_issue.project_id = :projectId and issue.estimated is not null", nativeQuery = true)
    List<String> findAllEstimativesByProjectId(@Param("projectId") Long projectId);

    @Query(value = "select distinct issue.system from issue"
            + " inner join issue_period_issue on issue_period_issue.issue_key = issue.key "
            + " where issue_period_issue.project_id = :projectId and issue.system is not null", nativeQuery = true)
    List<String> findAllSystemsByProjectId(@Param("projectId") Long projectId);

    @Query(value = "select distinct issue.epic from issue"
            + " inner join issue_period_issue on issue_period_issue.issue_key = issue.key "
            + " where issue_period_issue.project_id = :projectId and issue.epic is not null", nativeQuery = true)
    List<String> findAllEpicsByProjectId(Long projectId);
}
