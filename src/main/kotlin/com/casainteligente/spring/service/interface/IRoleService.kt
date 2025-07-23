package com.casainteligente.spring.service.`interface`

import com.casainteligente.spring.domain.dto.RoleDTO

interface IRoleService {
    fun criarPerfil(roleDto: RoleDTO)
}