package br.com.leonardoferreira.jirareport.repository;

import java.time.LocalDate;
import java.util.List;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira
 * @since 11/21/17 4:34 PM
 */
@Repository
public interface IssuePeriodRepository extends CrudRepository<IssuePeriod, Long> {

    @EntityGraph(attributePaths = { "issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig" }, type = EntityGraph.EntityGraphType.LOAD)
    List<IssuePeriod> findByBoardId(Long boardId);

    @EntityGraph(attributePaths = { "issues" })
    IssuePeriod findByStartDateAndEndDateAndBoardId(LocalDate startDate, LocalDate endDate, Long boardId);

    List<IssuePeriod> findByJqlIsNull();

}
