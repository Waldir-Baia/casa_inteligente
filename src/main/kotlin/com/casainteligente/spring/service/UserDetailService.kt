package com.casainteligente.spring.service

import com.casainteligente.spring.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByLogin(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado: $username")

        val authorities = user.roles.map { role ->
            SimpleGrantedAuthority("ROLE_${role.nome}")
        }.toSet()

        return org.springframework.security.core.userdetails.User(
            user.login,
            user.password,
            authorities
        )
    }
}