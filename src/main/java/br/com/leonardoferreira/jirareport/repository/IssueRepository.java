package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Issue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Long>, IssueCustomRepository {

    @Query(value = "SELECT DISTINCT issue.estimated FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.estimated IS NOT NULL", nativeQuery = true)
    List<String> findAllEstimativesByBoardId(Long boardId);

    @Query(value = "SELECT DISTINCT issue.system FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.system IS NOT NULL", nativeQuery = true)
    List<String> findAllSystemsByBoardId(Long boardId);

    @Query(value = "SELECT DISTINCT issue.epic FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.epic IS NOT NULL", nativeQuery = true)
    List<String> findAllEpicsByBoardId(Long boardId);

    @Query(value = "SELECT DISTINCT issue.issue_type FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.issue_type IS NOT NULL", nativeQuery = true)
    List<String> findAllIssueTypesByBoardId(Long boardId);

    @Query(value = "SELECT DISTINCT issue.project FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.project IS NOT NULL", nativeQuery = true)
    List<String> findAllIssueProjectsByBoardId(Long boardId);

    @Query("SELECT DISTINCT i FROM Issue i "
            + " JOIN i.issuePeriods ip "
            + " LEFT JOIN FETCH i.leadTimes leadTimes "
            + " LEFT JOIN FETCH leadTimes.leadTimeConfig "
            + " WHERE ip.id = :issuePeriodId")
    List<Issue> findByIssuePeriodId(Long issuePeriodId);

}
