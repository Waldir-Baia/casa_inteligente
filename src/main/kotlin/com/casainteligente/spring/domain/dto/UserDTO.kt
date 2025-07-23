package com.casainteligente.spring.domain.dto

data class UserDTO (
    val login: String,
    val url: String,
    val roles: Set<String> = emptySet()
)