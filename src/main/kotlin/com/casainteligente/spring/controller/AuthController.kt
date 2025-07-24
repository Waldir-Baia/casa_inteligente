package com.casainteligente.spring.controller

import com.casainteligente.spring.config.security.JwtTokenProvider
import com.casainteligente.spring.domain.dto.LoginRequestDTO
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
    private val  authenticationManager: AuthenticationManager,
    private val token: JwtTokenProvider
    ) {
    @PostMapping("/login")
    fun authenticationUser(@RequestBody loginRequest: LoginRequestDTO): ResponseEntity<*>{

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.name, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val jwt = token.generateToken(authentication)

        return ResponseEntity.ok("Token:" to jwt)
    }

}