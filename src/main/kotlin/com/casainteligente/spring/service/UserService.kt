package com.casainteligente.spring.service

import com.casainteligente.spring.repository.RoleRepository
import com.casainteligente.spring.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val web: WebClient
) {
}