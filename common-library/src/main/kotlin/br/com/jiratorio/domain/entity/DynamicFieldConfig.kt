package br.com.jiratorio.domain.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class DynamicFieldConfig(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    var board: Board,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var field: String

) : BaseEntity()
