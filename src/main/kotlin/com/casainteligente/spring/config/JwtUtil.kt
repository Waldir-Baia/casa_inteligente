package com.casainteligente.spring.config

import com.casainteligente.spring.domain.entity.Liberacao
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.Jwts
import javax.crypto.SecretKey // Import SecretKey
import java.util.Base64 // Import Base64 for encoding/decoding

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val base64Secret: String
) {
    private val SECRET_KEY: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret))

    fun gerarToken(liberacao: Liberacao): String {
        val hora = Date()
        val validade = Date(hora.time + 1000 * 60 * 60)

        return Jwts.builder()
            .setSubject(liberacao.nome)
            .setIssuedAt(hora)
            .setExpiration(validade)
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validarToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)

            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun extrairLogin(token: String): String {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}