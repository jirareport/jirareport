package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.UserConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfigRepository extends CrudRepository<UserConfig, Long> {

    UserConfig findByUsername(String username);

}
