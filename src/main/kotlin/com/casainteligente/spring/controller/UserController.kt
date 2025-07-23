package com.casainteligente.spring.controller

import com.casainteligente.spring.domain.dto.UserDTO
import com.casainteligente.spring.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/syncGit")
    fun sincronizarUsuarios(): ResponseEntity<String>{
        userService.buscarUsuariosGitHub()
        return ResponseEntity.ok("Sincronização concluída!")
    }

    @GetMapping
    fun listarUsuarios(): List<UserDTO>{
        return userService.listarUsuarios()
    }

    @PostMapping("/{userId}/roles/{roleId}")
    fun atribuirPerfil(
        @PathVariable userId: Long,
        @PathVariable roleId: Long,
    ): ResponseEntity<String>{
        userService.atribuirPerfil(userId, roleId)
        return ResponseEntity.ok("Perfil atribuído ao usuário!")
    }
}