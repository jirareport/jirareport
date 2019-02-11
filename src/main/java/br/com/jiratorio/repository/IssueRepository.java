package br.com.jiratorio.repository;

import br.com.jiratorio.domain.Issue;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Long>, IssueCustomRepository {

    @Query(value = "SELECT DISTINCT issue.estimated FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.estimated IS NOT NULL", nativeQuery = true)
    List<String> findAllEstimativesByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT DISTINCT issue.system FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.system IS NOT NULL", nativeQuery = true)
    List<String> findAllSystemsByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT DISTINCT issue.epic FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.epic IS NOT NULL", nativeQuery = true)
    List<String> findAllEpicsByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT DISTINCT issue.issue_type FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.issue_type IS NOT NULL", nativeQuery = true)
    List<String> findAllIssueTypesByBoardId(@Param("boardId") Long boardId);

    @Query(value = "SELECT DISTINCT issue.project FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.project IS NOT NULL", nativeQuery = true)
    List<String> findAllIssueProjectsByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT DISTINCT i FROM Issue i "
            + " JOIN i.issuePeriods ip "
            + " LEFT JOIN FETCH i.leadTimes leadTimes "
            + " LEFT JOIN FETCH leadTimes.leadTimeConfig "
            + " WHERE ip.id = :issuePeriodId")
    List<Issue> findByIssuePeriodId(@Param("issuePeriodId") Long issuePeriodId);

    @Query(value = "SELECT DISTINCT issue.priority FROM issue "
            + " WHERE issue.board_id = :boardId "
            + " AND issue.priority IS NOT NULL", nativeQuery = true)
    List<String> findAllIssuePrioritiesByBoardId(@Param("boardId") Long boardId);

}
