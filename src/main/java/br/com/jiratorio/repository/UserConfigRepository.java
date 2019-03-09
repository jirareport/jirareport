package br.com.jiratorio.repository;

import br.com.jiratorio.domain.entity.UserConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfigRepository extends CrudRepository<UserConfig, Long> {

    UserConfig findByUsername(String username);

}
