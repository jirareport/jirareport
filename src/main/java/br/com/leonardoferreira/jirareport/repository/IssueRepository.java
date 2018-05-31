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
            + " WHERE issue_period_issue.board_id = :boardId "
            + " AND issue.estimated IS NOT NULL", nativeQuery = true)
    List<String> findAllEstimativesByBoardId(Long boardId);

    @Query(value = "SELECT DISTINCT issue.system FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.board_id = :boardId "
            + " AND issue.system IS NOT NULL", nativeQuery = true)
    List<String> findAllSystemsByBoardId(Long boardId);

    @Query(value = "SELECT DISTINCT issue.epic FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.board_id = :boardId "
            + " AND issue.epic IS NOT NULL", nativeQuery = true)
    List<String> findAllEpicsByBoardId(Long boardId);

    @Query(value = "SELECT DISTINCT issue.issue_type FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.board_id = :boardId "
            + " AND issue.issue_type IS NOT NULL", nativeQuery = true)
    List<String> findAllIssueTypesByBoardId(Long boardId);

    @Query(value = "SELECT DISTINCT issue.project FROM issue "
            + " INNER JOIN issue_period_issue ON issue_period_issue.issue_key = issue.key "
            + " WHERE issue_period_issue.board_id = :boardId "
            + " AND issue.project IS NOT NULL", nativeQuery = true)
    List<String> findAllIssueProjectsByBoardId(Long boardId);

    @Query("SELECT DISTINCT i FROM Issue i "
            + " JOIN i.issuePeriods ip "
            + " LEFT JOIN FETCH i.leadTimes leadTimes "
            + " LEFT JOIN FETCH leadTimes.leadTimeConfig "
            + " WHERE ip.id.boardId = :boardId "
            + " AND ip.id.startDate = :startDate "
            + " AND ip.id.endDate = :endDate ")
    List<Issue> findByIssuePeriodId(Long boardId, String startDate, String endDate);
}
