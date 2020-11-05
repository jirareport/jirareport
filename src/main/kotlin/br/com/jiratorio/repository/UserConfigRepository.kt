package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.UserConfigEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserConfigRepository : CrudRepository<UserConfigEntity, Long> {

    fun findByUsername(username: String): UserConfigEntity?

}
