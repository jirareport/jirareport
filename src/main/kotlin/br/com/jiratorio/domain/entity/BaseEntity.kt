package br.com.jiratorio.domain.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity : Serializable {

    @CreatedBy
    var owner: String = ""

    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedBy
    var lastEditor: String = ""

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()

    companion object {
        private const val serialVersionUID = -818259944975210693L
    }

}
