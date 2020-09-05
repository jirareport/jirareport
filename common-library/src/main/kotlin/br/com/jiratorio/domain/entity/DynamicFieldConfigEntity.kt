package br.com.jiratorio.domain.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "dynamic_field_config")
data class DynamicFieldConfigEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    var board: BoardEntity,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var field: String

) : BaseEntity()
