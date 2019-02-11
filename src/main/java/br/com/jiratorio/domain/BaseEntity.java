package br.com.jiratorio.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "int-array",    typeClass = IntArrayType.class),
        @TypeDef(name = "json",         typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb",        typeClass = JsonBinaryType.class),
        @TypeDef(name = "jsonb-node",   typeClass = JsonNodeBinaryType.class),
        @TypeDef(name = "json-node",    typeClass = JsonNodeStringType.class),
})
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -818259944975210693L;

    @CreatedBy
    protected String owner;

    @CreationTimestamp
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    @LastModifiedBy
    protected String lastEditor;

}
