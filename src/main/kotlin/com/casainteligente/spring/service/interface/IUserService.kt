package com.casainteligente.spring.service.`interface`

import com.casainteligente.spring.domain.dto.UserDTO

interface IUserService {
    fun buscarUsuariosGitHub()
    fun listarUsuarios(): List<UserDTO>
    fun atribuirPerfil(idUser: Long, idRole: Long)
}