package com.casainteligente.spring.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "liberacao")
open class Liberacao() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(nullable = false)
    lateinit var nome: String

    @Column(nullable = false)
    lateinit var senha: String

    constructor(nome: String, senha: String) : this() {
        this.nome = nome
        this.senha = senha
    }
}