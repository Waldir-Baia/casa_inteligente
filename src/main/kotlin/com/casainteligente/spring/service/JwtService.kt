package com.casainteligente.spring.service

import com.casainteligente.spring.repository.UserRepository

class JwtService(private val userRepository: UserRepository): User {
}