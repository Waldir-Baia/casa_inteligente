package com.casainteligente.spring.controller

import com.casainteligente.spring.domain.dto.RoleDTO
import com.casainteligente.spring.service.RoleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/roles")
class RoleController(private val roleService: RoleService) {

    @PostMapping
    fun criarPerfil(@RequestBody roleDto: RoleDTO): ResponseEntity<String>{
        roleService.criarPerfil(roleDto)
        return ResponseEntity.ok("Perfil criado!")
    }
}