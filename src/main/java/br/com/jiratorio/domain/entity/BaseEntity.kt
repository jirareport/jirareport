package br.com.jiratorio.domain.entity

import com.vladmihalcea.hibernate.type.array.IntArrayType
import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@TypeDefs(
        TypeDef(name = "string-array", typeClass = StringArrayType::class),
        TypeDef(name = "int-array", typeClass = IntArrayType::class),
        TypeDef(name = "json", typeClass = JsonStringType::class),
        TypeDef(name = "jsonb", typeClass = JsonBinaryType::class),
        TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType::class),
        TypeDef(name = "json-node", typeClass = JsonNodeStringType::class)
)
open class BaseEntity : Serializable {
    companion object {
        private const val serialVersionUID = -818259944975210693L
    }

    @CreatedBy
    var owner: String? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

    @LastModifiedBy
    var lastEditor: String? = null

}
