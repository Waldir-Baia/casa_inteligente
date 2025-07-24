package com.casainteligente.spring.config.security

import JwtAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class JwtConfig(
    private val jwtAutentication: JwtAuthenticationFilter,
    private val userDetalheService: UserDetailsService,
   //private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) {

    @Bean
    fun jwtAuthenticationEntryPoint(): JwtAuthenticationEntryPoint {
        return JwtAuthenticationEntryPoint()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .exceptionHandling { it.authenticationEntryPoint(jwtAuthenticationEntryPoint()) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/swagger-ui.html").permitAll()
                auth.requestMatchers("/swagger-ui/**").permitAll()
                auth.requestMatchers("/v3/api-docs/**").permitAll()
                auth.requestMatchers("/swagger-resources/**").permitAll()
                auth.requestMatchers("/webjars/**").permitAll()

                // Permissões Autenticação e Sincronização
                auth.requestMatchers("/auth/**").permitAll()
                auth.requestMatchers("/users/syncGit").permitAll()

                auth.anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAutentication, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun criptografaSenha(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(userDetailsService: UserDetailsService, passwordEncoder: PasswordEncoder): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        return ProviderManager(authenticationProvider)
    }
}