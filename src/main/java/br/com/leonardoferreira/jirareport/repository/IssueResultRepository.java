package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.IssueResult;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author leferreira
 * @since 11/21/17 4:34 PM
 */
public interface IssueResultRepository extends MongoRepository<IssueResult, IssueForm> {

    @Query("{ '_id.projectId': ?0 }")
    List<IssueResult> findByProjectId(Long projectId);

}
