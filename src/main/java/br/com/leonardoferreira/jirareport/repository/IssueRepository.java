package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Issue;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<Issue, String>, IssueCustomRepository {

    @Query(value = "SELECT DISTINCT issue.estimated FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.project_id = :projectId "
            + " AND issue.estimated IS NOT NULL", nativeQuery = true)
    List<String> findAllEstimativesByProjectId(Long projectId);

    @Query(value = "SELECT DISTINCT issue.system FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.project_id = :projectId "
            + " AND issue.system IS NOT NULL", nativeQuery = true)
    List<String> findAllSystemsByProjectId(Long projectId);

    @Query(value = "SELECT DISTINCT issue.epic FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.project_id = :projectId "
            + " AND issue.epic IS NOT NULL", nativeQuery = true)
    List<String> findAllEpicsByProjectId(Long projectId);

    @Query(value = "SELECT DISTINCT issue.issue_type FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.project_id = :projectId "
            + " AND issue.issue_type IS NOT NULL", nativeQuery = true)
    List<String> findAllIssueTypesByProjectId(Long projectId);

    @Query(value = "SELECT DISTINCT issue.project FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.project_id = :projectId "
            + " AND issue.project IS NOT NULL", nativeQuery = true)
    List<String> findAllIssueProjectsByProjectId(Long projectId);

    @Query("SELECT DISTINCT i FROM Issue i "
            + " JOIN i.issuePeriods ip "
            + " JOIN FETCH i.leadTimes leadTimes "
            + " JOIN FETCH leadTimes.leadTimeConfig "
            + " WHERE ip.id.projectId = :projectId "
            + " AND ip.id.startDate = :startDate "
            + " AND ip.id.endDate = :endDate ")
    List<Issue> findByIssuePeriodId(Long projectId, String startDate, String endDate);
}
