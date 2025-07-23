package com.casainteligente.spring.repository

import com.casainteligente.spring.domain.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long>{

    fun findByNome(nome: String): Role?
}