package com.casainteligente.spring.service

import com.casainteligente.spring.domain.dto.RoleDTO
import com.casainteligente.spring.domain.entity.Role
import com.casainteligente.spring.repository.RoleRepository
import com.casainteligente.spring.service.`interface`.IRoleService
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) : IRoleService {

    override fun criarPerfil(roleDto: RoleDTO) {
        val existeRole = roleRepository.buscarPeloNome(roleDto.nome)
        if(existeRole != null){
            throw RuntimeException("Perfil já existe!")
        }
        val role = Role(name = roleDto.nome)
        roleRepository.save(role)
    }
}