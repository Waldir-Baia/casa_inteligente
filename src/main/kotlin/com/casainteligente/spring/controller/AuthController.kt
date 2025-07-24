package com.casainteligente.spring.controller

import com.casainteligente.spring.domain.dto.LiberacaoDTO
import com.casainteligente.spring.service.LiberacaoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val liberacaoService: LiberacaoService
) {
    @PostMapping("/liberacao")
    fun login(@RequestBody liberacaoDTO: LiberacaoDTO): ResponseEntity<Map<String, String>>{
        return try {
            val token = liberacaoService.autenticar(liberacaoDTO.nome, liberacaoDTO.senha)
            ResponseEntity.ok(mapOf("token" to token))
        }catch (e: RuntimeException){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("erro" to e.message!!))
        }
    }

}