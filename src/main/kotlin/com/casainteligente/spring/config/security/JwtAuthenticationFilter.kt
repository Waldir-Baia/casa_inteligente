package com.casainteligente.spring.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class JwtAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    private val logger: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = getJwtFromRequest(request)
            if (StringUtils.hasText(jwt)) {
                if (tokenProvider.validateToken(jwt!!)) {
                    val username = tokenProvider.getUsernameFromJwt(jwt)
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                } else {
                    SecurityContextHolder.clearContext()
                    logger.warn("Token JWT inválido para a requisição: ${request.requestURI}")
                }
            }
        } catch (ex: Exception) {
            logger.error("Erro de autenticação para a requisição: ${request.requestURI}", ex)
            SecurityContextHolder.clearContext()
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (StringUtils.hasText(bearerToken) && bearerToken!!.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}