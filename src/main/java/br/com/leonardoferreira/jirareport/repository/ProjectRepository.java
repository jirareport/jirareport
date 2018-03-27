package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira
 * @since 3/23/18 6:21 PM
 */
@Repository
public interface ProjectRepository extends MongoRepository<Project, Long> {

}
