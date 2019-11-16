package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.UserConfig
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserConfigRepository : CrudRepository<UserConfig, Long> {

    fun findByUsername(username: String): UserConfig?

}
