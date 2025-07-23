package com.casainteligente.spring.config.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {

    @Value("\${app.jwtSecret}")
    private lateinit var jwtSecret: String

    @Value("\${app.jwtExpirationMs}")
    private var jwtExpirationMs: Long = 0

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as org.springframework.security.core.userdetails.User
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUsernameFromJwt(token: String): String {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
    }

    fun validateToken(authToken: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            true
        } catch (ex: Exception) {
            false
        }
    }
}