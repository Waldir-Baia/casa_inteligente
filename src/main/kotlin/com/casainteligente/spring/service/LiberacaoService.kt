package com.casainteligente.spring.service

import com.casainteligente.spring.config.JwtUtil
import com.casainteligente.spring.repository.LiberacaoRepository
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class LiberacaoService(
    private val liberacaoRepository: LiberacaoRepository,
    private val jwtUtil: JwtUtil
) {
    fun autenticar(nome: String, senha: String): String {
        val usuario = liberacaoRepository.findByNome(nome)
            ?: throw RuntimeException("Usuário não encontrado")

        if(!BCrypt.checkpw(senha, usuario.senha)){
            throw RuntimeException("Senha inválida")
        }

        return jwtUtil.gerarToken(usuario)
    }

}