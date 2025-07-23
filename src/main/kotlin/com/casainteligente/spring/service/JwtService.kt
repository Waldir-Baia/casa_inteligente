package com.casainteligente.spring.service

import com.casainteligente.spring.config.security.JwtTokenProvider
import com.casainteligente.spring.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class JwtService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager
    // private val customUserDetailsService: CustomUserDetailsService
) {

    fun authenticateAndGenerateToken(username: String, password: String): String {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )
        return jwtTokenProvider.generateToken(authentication)
    }
}