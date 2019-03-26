package br.com.jiratorio.repository;

import br.com.jiratorio.domain.entity.IssuePeriod;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuePeriodRepository extends CrudRepository<IssuePeriod, Long> {

    @EntityGraph(attributePaths = { "issues", "issues.leadTimes", "issues.leadTimes.leadTimeConfig" }, type = EntityGraph.EntityGraphType.LOAD)
    List<IssuePeriod> findByBoardId(Long boardId);

    @EntityGraph(attributePaths = { "issues" })
    IssuePeriod findByStartDateAndEndDateAndBoardId(LocalDate startDate, LocalDate endDate, Long boardId);

}
