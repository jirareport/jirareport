package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author lferreira
 * @since 11/21/17 4:34 PM
 */
public interface IssuePeriodRepository extends MongoRepository<IssuePeriod, IssueForm> {

    @Query("{ '_id.projectId': ?0 }")
    List<IssuePeriod> findByProjectId(Long projectId);

}
