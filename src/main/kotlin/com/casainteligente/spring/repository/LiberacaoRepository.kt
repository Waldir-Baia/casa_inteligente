package com.casainteligente.spring.repository

import com.casainteligente.spring.domain.entity.Liberacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LiberacaoRepository : JpaRepository<Liberacao, Long> {
    fun findByNome(nome: String): Liberacao?
}