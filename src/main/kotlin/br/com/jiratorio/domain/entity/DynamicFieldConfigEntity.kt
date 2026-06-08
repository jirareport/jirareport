package br.com.jiratorio.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

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
