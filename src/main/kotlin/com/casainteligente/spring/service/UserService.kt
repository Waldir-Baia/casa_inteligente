package com.casainteligente.spring.service

import com.casainteligente.spring.domain.dto.UserDTO
import com.casainteligente.spring.domain.entity.User
import com.casainteligente.spring.repository.RoleRepository
import com.casainteligente.spring.repository.UserRepository
import com.casainteligente.spring.service.`interface`.IUserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val webClient: WebClient
) : IUserService {
    override fun buscarUsuariosGitHub() {
        val usuarioGitHub = webClient.get()
            .uri("https://api.github.com/users")
            .retrieve()
            .bodyToFlux(UserDTO::class.java)
            .collectList()
            .block() ?: emptyList()

        usuarioGitHub.forEach { result ->
            val existeUsuario = userRepository.findByLogin(result.login)
            if(existeUsuario == null){
                val user = User(
                    login = result.login,
                    url = result.url
                )
                userRepository.save(user)
            }
        }
    }

    override fun listarUsuarios(): List<UserDTO> {
        val usuarios = userRepository.findAll()
        return usuarios.map { user ->
            UserDTO(
                login = user.login,
                url = user.url,
                roles = user.roles.map { it.nome }.toSet()
            )
        }
    }

    @Transactional
    override fun atribuirPerfil(idUser: Long, idRole: Long) {
        val user = userRepository.findById(idUser)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        val role = roleRepository.findById(idRole)
            .orElseThrow { RuntimeException("Perfil não encontrado") }

        user.roles.add(role)
        userRepository.save(user)
    }
}