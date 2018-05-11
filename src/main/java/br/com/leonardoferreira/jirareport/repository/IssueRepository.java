package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Issue;
import org.springframework.data.repository.CrudRepository;

public interface IssueRepository extends CrudRepository<Issue, String>, IssueCustomRepository {
}
